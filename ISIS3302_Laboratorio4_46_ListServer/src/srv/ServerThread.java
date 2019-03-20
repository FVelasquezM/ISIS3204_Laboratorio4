package srv;

import java.io.PrintWriter;
import java.net.Socket;


import logic.VideoContainer;


public class ServerThread extends Thread{

	private static ListServer server;

	private final int threadNumber;

	private int activeNumber;

	private Socket s; 

	protected static void setServer(ListServer s) {
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
			System.out.println("Thread " + threadNumber + " active | connected to " + s.getInetAddress());
			
			PrintWriter out;
			out = new PrintWriter(s.getOutputStream(), true);
			
			//retorna una lista de videos
			//en la forma VID:<NAME>:<IP>:<PUERTO>
			//indicando el nombre del video además de la ip y el puerto en el que se
			//encuentra
			
			//Leer listado de videos y enviarlo a cliente
			new VideoContainer().ListVideos(out);
			
			System.out.println("Videos listed");
			
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
