package view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import uk.co.caprica.vlcj.media.callback.CallbackMedia;
import uk.co.caprica.vlcj.media.callback.seekable.RandomAccessFileMedia;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class VideoPlayer {
	
    private final JFrame frame;
	
    private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
    
    private static long vidStart;
    
    private static long lastUpdate;
	
	private void PlayVideo(boolean first) {
		
		if(first) {
			CallbackMedia media = new RandomAccessFileMedia(new File("stream.wmv"));
			System.out.println(mediaPlayerComponent.mediaPlayer().media().start(media));
			vidStart = System.currentTimeMillis();
		}
		else {
			CallbackMedia media = new RandomAccessFileMedia(new File("stream.wmv"));
			mediaPlayerComponent.mediaPlayer().media().prepare(media);
			System.out.println(mediaPlayerComponent.mediaPlayer().media().start(media));
			mediaPlayerComponent.mediaPlayer().controls().setTime(mediaPlayerComponent.mediaPlayer().status().time());	
		}
	 }
	
	
	public VideoPlayer(String host, int port) {

	 	frame = new JFrame("ElSuperStream");
    	frame.setBounds(100, 100, 600, 400);
    	frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	    
    	mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    	frame.setContentPane(mediaPlayerComponent);
	    
    	frame.setVisible(true);
		
		try {
			//PrintStream fileOut = new PrintStream("./out.txt");
			//System.setOut(fileOut);
			Socket s = new Socket(host, port);
			

				
			while(true) {
				InputStream di =  s.getInputStream();
				
				File f = new File("stream.wmv");
				FileOutputStream fo = new FileOutputStream(f);
				
				int chunckSize = 64;
				
				int inByte = -1;
				byte[] inBytes = new byte[chunckSize];

				int i = 0;
				
				while((inByte = di.read(inBytes, 0, chunckSize)) != -1) {
					fo.write(inBytes, 0, inByte);
					
					if(i == 1024) {
						this.PlayVideo(true);
						lastUpdate = vidStart;
					}
					else if(i > 1024 && (System.currentTimeMillis() - lastUpdate) >= 200) {
						lastUpdate = System.currentTimeMillis();
						this.PlayVideo(false);
						System.out.println("Refresh vid");
					}
					i++;
				}
				fo.flush();
				fo.close();

				this.PlayVideo(false);
				
				s.close();
				break;
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
