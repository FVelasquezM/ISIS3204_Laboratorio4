package srv;

import java.net.Socket;


import logic.VideoUtility;

public class ServerThread extends Thread{

	private static StreamServer server;

	private final int threadNumber;

	private int activeNumber;

	private Socket s; 

	protected static void setServer(StreamServer s) {
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
			//PrintWriter out = new PrintWriter(s.getOutputStream());
			System.out.println("Thread " + threadNumber + " active");

			System.out.println("Video LOADED");
			
			//Enviar STR indicando que se iniciará el envío del video.
			//out.println("STR");
			
			//Enviar video
			VideoUtility.SendVideo(VideoUtility.LoadVideo(), s.getOutputStream());
			
			//Indica al cliente que se ha acabado la transmisión de frames.
			//out.println("STP");
			System.out.println("MOVIE SENT");
			
			//out.close();
			s.close();

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {

		while(true) {

			//Thread inicia dormido.
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
