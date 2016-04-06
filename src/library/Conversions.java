package library;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Conversions {
	
	public static BufferedImage byteArrayToImage( int length, DataInputStream input ) throws IOException{
		byte[] tmp = new byte[ length ];
		BufferedImage image = null;
		try {
			input.readFully(tmp, 0, length);
			image = ImageIO.read(new ByteArrayInputStream(tmp));
        } catch (Exception e) {
			e.printStackTrace();
		}

        return image;
	}

    public static byte[] imageToByteArray ( BufferedImage image ) throws IOException{
        ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", arrayOutput);
        byte[] a = arrayOutput.toByteArray();

        return a;
    }
}