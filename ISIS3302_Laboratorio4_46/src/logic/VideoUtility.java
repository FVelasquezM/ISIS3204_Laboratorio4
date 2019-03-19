package logic;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;

/**
 * Carga un archivo de video a memoria principal
 * @author Felipe Velásquez
 *
 */
public class VideoUtility {

	public static FileInputStream LoadVideo() {

		try {
			File file = new File("video.mp4");

			return new FileInputStream(file);	
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;


	}

	public static void SendVideo(FileInputStream video, OutputStream out) {
		
		System.out.println("Sending Video...");
		
		
		
		try {
			
			File f = new File("sirve.mp4");
			FileOutputStream fo = new FileOutputStream(f);
			
			int chunckSize = 256;
			
			byte[] vBytes = new byte[chunckSize];
			
			int readBytes = -1;
			
			do {
				readBytes = video.read(vBytes, 0, chunckSize);
				if(readBytes != -1)
					fo.write(vBytes, 0, readBytes);
					out.write(vBytes, 0, readBytes);
			}
			while(readBytes != -1);
			
			fo.close();
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Video Sent!");

	}

}
