package PT.Pcapture;

import java.util.ArrayList;
import java.util.List;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.PcapIf;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.PcapBpfProgram;
import org.jnetpcap.protocol.tcpip.Http;

/**
 * Created by Jakub on 12.04.2016.
 */
public class OpenedPages implements Runnable{
    public  List<PcapIf> alldevs;
    public  StringBuilder errbuf;
    public  PcapIf Device;
    private  ArrayList<Thread> MyThreads;

    public OpenedPages(){}

    public OpenedPages(PcapIf Device, StringBuilder errbuf)
    {
        this.errbuf = errbuf;
        this.Device = Device;
    }
    public   void StopCapturing(){
        //Zatrzymuje wszystkie wątki łapiące pakiety
        for (Thread t : MyThreads) {
            t.stop();
        }
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
        System.out.println("Network devices found:");
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
            Runnable thread = new OpenedPages(device, errbuf);

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
        String description = (SelectedDevice.getDescription() != null) ? SelectedDevice
                .getDescription() : "No description available";
        System.out.printf(" %s [%s]\n", SelectedDevice.getName(),
                description);

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
                if(packet.hasHeader(h)){

                    String a =  h.fieldValue((Http.Request.Host.Host));
                    String aa =  h.fieldValue((Http.Request.Host.RequestUrl));

                    if(a != null) {
                        System.out.println(a + aa);
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
        /*
        PcapBpfProgram filter = new PcapBpfProgram();
        String expression = "tcp port 80 or tcp port 443";//port http or https
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
*/
        // capture first 10 packages
        pcap.loop(pcap.LOOP_INFINATE, jpacketHandler, "jNetPcap");
        pcap.close();
    }
}
