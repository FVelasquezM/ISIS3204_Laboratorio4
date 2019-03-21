package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import logic.Video;

public class Interface {
	
	private ArrayList<JButton> buttons;
	
	public Interface(ArrayList<Video> vids, ActionListener main) {
		
		buttons = new ArrayList<JButton>();
		
		GenerateInterface(vids, main);
		
	}
	
	private void GenerateInterface(ArrayList<Video> videos, ActionListener ac) {
		
		JFrame f=new JFrame("TCP Streaming Client Marroquín-Silva-Velásquez");  
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int y = 20;
		for(Video v : videos) {
			JButton b=new JButton(v.getName());  
			b.setBounds(50,y+=50,300,30);  
		    f.add(b);
		    b.addActionListener(ac);
		    buttons.add(b);
		}
	    
	    f.setSize(400,400);  
	    f.setLayout(null);  
	    f.setVisible(true);   
	}
	
	public ArrayList<JButton> getButtons(){
		return this.buttons;
	}
	
}
