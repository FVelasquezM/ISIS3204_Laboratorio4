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
			File file = new File("samplewmv.wmv");

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
			
			int chunckSize = 2048;
			
			byte[] vBytes = new byte[chunckSize];
			
			int readBytes = -1;
			
			while((readBytes = video.read(vBytes, 0, chunckSize)) != -1){
				out.write(vBytes, 0, readBytes);
			}
			
			out.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Video Sent!");

	}

}
