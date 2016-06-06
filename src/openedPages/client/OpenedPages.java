package openedPages.client;

import java.util.ArrayList;
import java.util.List;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import java.util.regex.*;
import java.net.*;
/**
 * Created by Jakub on 12.04.2016.
 */
public class OpenedPages implements Runnable{
    public  List<PcapIf> alldevs;
    public  StringBuilder errbuf;
    public  StringBuilder stream;
    public  PcapIf Device;
    public  String prev = "";
    public  String prevs = "";
    private  ArrayList<Thread> MyThreads;

    public OpenedPages( StringBuilder stream){
        this.stream = stream;
    }

    public OpenedPages(PcapIf Device, StringBuilder errbuf, StringBuilder stream)
    {
        this.errbuf = errbuf;
        this.Device = Device;
        this.stream = stream;
    }
    public   void StopCapturing(){
        //Zatrzymuje wszystkie wątki łapiące pakiety
        for (Thread t : MyThreads) {
            t.stop();
        }
        //stream.close();
    }
    public  void StartCapturing(){
        alldevs = new ArrayList<PcapIf>(); // Will be filled with NICs
        errbuf = new StringBuilder(); // For any error msgs
        int r = Pcap.findAllDevs(alldevs, errbuf);
        if (r != Pcap.OK || alldevs.isEmpty()) {
            System.err.printf("Can't read list of devices, error is %s",
                    errbuf.toString());
            return;
        }
        //System.out.println("Network devices found:");
        /*int i = 0;
        for (PcapIf device : alldevs) {
            String description = (device.getDescription() != null) ? device
                    .getDescription() : "No description available";
            System.out.printf("#%d: %s [%s]\n", i++, device.getName(),
                    description);
        }*/
        //mam devices, złapać pakiety z wszystkich
        ArrayList<Thread> threads = new ArrayList<>();
        for (PcapIf device : alldevs) {
            Runnable thread = new OpenedPages(device, errbuf, stream);

            Thread t = new Thread(thread);

            threads.add(t);

            t.start();
        }
        MyThreads = threads;
    }

    @Override
    public void run() {
        CapturePackets(Device, errbuf);
    }

    private  void CapturePackets(PcapIf SelectedDevice, StringBuilder errbuf ){
        //String description = (SelectedDevice.getDescription() != null) ? SelectedDevice
        //        .getDescription() : "No description available";
        //System.out.printf(" %s [%s]\n", SelectedDevice.getName(),
        //        description);

        PcapIf device = SelectedDevice; // Get first device in list
        int snaplen = 64 * 1024; // Capture all packets, no trucation
        int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
        int timeout = 120 * 1000; // 10 seconds in millis
        Pcap pcap = Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf);
        if (pcap == null) {
            System.err.printf("Error while opening device for capture: "
                    + errbuf.toString());
            return;
        }
        PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {
            public void nextPacket(PcapPacket packet, String user) {
                Http h = new Http();
                Tcp t = new Tcp();
                Ip4 ip = new Ip4();

                if(packet.hasHeader(h)){

                    String a =  h.fieldValue((Http.Request.Host.Host));
                    String aa =  h.fieldValue((Http.Request.Host.RequestUrl));

                    if(a!= null) {
                        Pattern p = Pattern.compile("([a-zA-Z]+\\.)?[\\w]+\\.[a-zA-Z]+");
                        Matcher m = p.matcher(a);
                        boolean b = m.matches();

                        if (!a.equals(prev) && b == true) {
                            //System.out.println(a);
                            stream.append(a + "\n");
                            prev = a;
                        } else {//
                            if (packet.hasHeader(ip)) {
                                String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(packet.getHeader(ip).destination());

                                InetAddress addr = null;
                                try {
                                    addr = InetAddress.getByName(destinationIP);
                                } catch (UnknownHostException e) {
                                    e.printStackTrace();
                                }
                                String host = addr.getHostName();



                                m = p.matcher(host);
                                b = m.matches();

                                if(!host.equals(prevs) ) {
                                    //System.out.println("https: " + host);
                                    stream.append(a + "\n");
                                    prevs = host;
                                }

                            }
                        }
                    }

                }
               /* String sourceIP = org.jnetpcap.packet.format.FormatUtils.ip(sIP);
                String destinationIP = org.jnetpcap.packet.format.FormatUtils.ip(dIP);

                System.out.println("srcIP=" + sourceIP +
                        " dstIP=" + destinationIP +
                        " caplen=" + packet.getCaptureHeader().caplen());*/
            }
        };
        //set filter

        PcapBpfProgram filter = new PcapBpfProgram();
        String expression = "tcp dst port 80 or tcp dst port 443";//port http or https
        int optimize = 0; // 0 = false
        int netmask = 0xFFFFFF00; // 255.255.255.0
        if (pcap.compile(filter, expression, optimize, netmask) != Pcap.OK) {
            System.err.println(pcap.getErr());
            return;
        }
        if (pcap.setFilter(filter) != Pcap.OK) {
            System.err.println(pcap.getErr());
            return;
        }

        // capture first 10 packages
        pcap.loop(pcap.LOOP_INFINATE, jpacketHandler, "jNetPcap");
        pcap.close();
    }
}
