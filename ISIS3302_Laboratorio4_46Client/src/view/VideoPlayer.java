package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class VideoPlayer {

	public VideoPlayer(String host, int port) {


		try {
			Socket s = new Socket(host, port);

			while(true) {
				InputStream di =  s.getInputStream();

				String filename = "stream-"+host+":"+port+".wmv";
				File f = new File(filename);
				FileOutputStream fo = new FileOutputStream(f);

				int chunckSize = 64;

				int inByte = -1;
				byte[] inBytes = new byte[chunckSize];

				int i = 0;
				
				Process p = null;
				
				while((inByte = di.read(inBytes, 0, chunckSize)) != -1) {
					fo.write(inBytes, 0, inByte);

					if(i++ == 0) {
						ProcessBuilder pb = new ProcessBuilder("/usr/bin/vlc", filename);
						p = pb.start();
					}

				
				}
				
				fo.flush();
				fo.close();
			
				//this.PlayVideo(false);

				s.close();
				
				//espera a que el usuario acabe de ver el 
				p.waitFor();
				
				//elimina el video del sistema
				f.delete();
				break;
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}


}
