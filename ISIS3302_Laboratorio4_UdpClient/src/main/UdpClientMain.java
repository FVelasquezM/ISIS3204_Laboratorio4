package main;

import java.util.ArrayList;

import logic.SelectorController;
import logic.Video;
import logic.VideosList;
import view.Interface;

public class UdpClientMain {

	public static void main(String[] args) {
		
		
		//Pedir videos al servidor de información;
		ArrayList<Video> vids = VideosList.getVideos();
		
		//Controlador de selecció en la interfaz
		SelectorController sc = new SelectorController(vids);

		//Generar interfaz simple según videos
		Interface i = new Interface(vids,sc);
		
		sc.setButtons(i.getButtons());
	}

	
	
	
	
}
