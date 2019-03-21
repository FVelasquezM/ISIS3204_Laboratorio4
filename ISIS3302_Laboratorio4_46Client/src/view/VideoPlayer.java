package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

public class VideoPlayer {

	public VideoPlayer(String host, int port) {


		try {
			System.out.println("creating sock");
			Socket s = new Socket(host, port);
			System.out.println("Socket done");
			while(true) {
				InputStream di =  s.getInputStream();
				System.out.println("Connecting to: " + host);
				String filename = "stream-"+host+":"+port+".wmv";
				File f = new File(filename);
				FileOutputStream fo = new FileOutputStream(f);

				int chunckSize = 1024;

				int inByte = -1;
				byte[] inBytes = new byte[chunckSize];

				int i = 0;
				
				Process p = null;
				
				while((inByte = di.read(inBytes, 0, chunckSize)) != -1) {
					fo.write(inBytes, 0, inByte);

					if(++i == 1) {
						System.out.println("Opening VLC");
						ProcessBuilder pb = new ProcessBuilder("/usr/bin/vlc", filename, "-L");
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
