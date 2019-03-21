package logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * Carga un archivo de video a memoria principal
 * @author Felipe Velásquez
 *
 */
public class VideoUtility {

	public static FileInputStream LoadVideo() {

		try {
			File file = new File(Config.videoFile);

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
			
			int chunckSize = 1024;
			
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
		
	}

}
