import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 * @author felipe
 *
 */
public class Uploader {
	
	//Recibe (por TCP) un video.
	/**
	 * 
	 * @param s
	 * @param filename corresponde a un video wmv
	 */
	public static void GetUpload(Socket s, String filename) {

		try {
			InputStream di = s.getInputStream();
			File f = new File(filename);
			FileOutputStream fo = new FileOutputStream(f);
			int chunckSize = 1024;

			int inByte = -1;
			byte[] inBytes = new byte[chunckSize];

			Process p = null;
			
			while((inByte = di.read(inBytes, 0, chunckSize)) != -1) {
				fo.write(inBytes, 0, inByte);
				//System.out.println(inByte);

			
			}
		
			di.close();
			fo.flush();
			fo.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
