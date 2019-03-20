package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

//Contiene todos los videos del servidor
//Clase Singleton
public class VideoContainer {

	private ArrayList<Video> videos;
	
	
	//Idealmente, cargar videos de un archivo.
	public VideoContainer() {
		
		//Archivo debe ser un archivo de texto en el formato
		//VID:<NAME>:<IP>:<PORT>
		try {
			BufferedReader in =  new BufferedReader(new FileReader(new File("videos.txt")));
			
			videos = new ArrayList<Video>();
			
			String inLine;
			String[] parts;
			while((inLine = in.readLine()) != null) {
				parts = inLine.split(":");
				videos.add(new Video(parts[1], parts[2], Integer.parseInt(parts[3])));
			}
			
			System.out.println("Loaded " + videos.size() + " videos");
			in.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void ListVideos(PrintWriter pw) {
		System.out.println("Listing videos");
		for(Video v : videos) {
			System.out.println(v.toString());
			pw.println(v.toString());
		}
		
	}
	
}
