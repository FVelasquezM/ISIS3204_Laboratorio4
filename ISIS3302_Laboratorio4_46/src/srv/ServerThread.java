package srv;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import logic.VideoLoader;

public class ServerThread extends Thread{

	private static Server server;

	private final int threadNumber;

	private int activeNumber;

	private Socket s; 

	protected static void setServer(Server s) {
		server = s;
	}

	public ServerThread(int i) {
		this.threadNumber = i;
		this.activeNumber = -1;
		System.out.println("Created Thread " + i);
	}

	protected void setSocket(Socket s) {
		this.s = s;
	}

	protected void setActiveNumber(int activeNumber) {
		this.activeNumber = activeNumber;
	}

	protected void attend() {

		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.println("Thread " + threadNumber + " active");

			ArrayList<Picture> frames = VideoLoader.LoadVideo();
			System.out.println("Video LOADED");
			
			//Enviar STR indicando que se iniciará el envío del video.
			out.println("STR");
			
			BufferedImage bf;
			ByteArrayOutputStream ba;
			int i = 0;
			System.out.println("FRAMES TO SEND: " + frames.size());
			for(Picture p : frames) {
				//Enviar FRM indicando el inicio de un frame
				out.println("FRM");
				
				//Preparar frame para envío
				bf = AWTUtil.toBufferedImage(p);
				ba = new ByteArrayOutputStream();
				ImageIO.write(bf, "png", ba);
				
				//Enviar tamaño de la información para preparar al receptor
				out.println("SIZ " + ba.size());
				
				//Envíar imagen
				ba.writeTo(s.getOutputStream());
				System.out.println("Frame " + i++ + " sent | size: " + ba.size());
				
				//FRAME END (sirve de buffer entre frames, ninguna otra función)
				out.println("FRE");
			}
			//Indica al cliente que se ha acabado la transmisión de frames.
			out.println("STP");
			System.out.println("MOVIE SENT");
			
			out.close();
			s.close();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while(true) {

			//Thread inicia dormido.
			//
			synchronized(this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			attend();


			//Notificar al servidor que ha acabado, 
			//para así ser puesto en la fila de espera.
			server.threadDone(activeNumber);
			synchronized(this) {
				this.activeNumber = -1;
				this.s = null;
			}
		}
	}


}
