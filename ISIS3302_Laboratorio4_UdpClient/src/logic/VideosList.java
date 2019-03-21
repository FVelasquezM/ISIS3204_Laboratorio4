package logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class VideosList {

	public static ArrayList<Video> getVideos() {

		try {
			
			//Se conecta con el servidor para pedirle el listado de videos
			Socket s = new Socket("localhost", 1234);
			BufferedReader in; 
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			System.out.println("Waiting for video list...");
			
			ArrayList<Video> videos = new ArrayList<Video>();
			
			String inLine;
			String[] parts;
			while((inLine = in.readLine()) != null) {
				
				System.out.println(inLine);
				parts = inLine.split(":");
				videos.add(new Video(parts[1], parts[2], Integer.parseInt(parts[3])));
			
				
			}

			
			s.close();
			
			return videos;
		}
		catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}

}
