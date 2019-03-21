package logic;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

import view.UdpVideoPlayer;

public class SelectorController  implements ActionListener{

	//Si el evento es disparado por el primer botón, se accede al primer
	//video. Segundo botón. segundo video, etc..
	private ArrayList<Video> videos;
	
	private ArrayList<JButton> buttons;
	
	public SelectorController(ArrayList<Video> videos) {
		this.videos = videos;
	}
	
	public void setButtons(ArrayList<JButton> buttons) {
		this.buttons = buttons;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		for(int i = 0; i<videos.size() && i<buttons.size(); i++) {
			
			if(e.getSource() == buttons.get(i)) {
				//Play video videos.get(i)
				System.out.println("Pressed " + i);
				Video v = videos.get(i);
				UdpVideoPlayer.play(v.getIp(), v.getPort());
				break;
			}
			
		}
		
	}

}
