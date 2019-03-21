package srv;

import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramSocket;
import java.net.Socket;

import logic.UdpVideoUtility;



public class ServerThread extends Thread{

	private static UdpServer server;

	private final int threadNumber;

	private int activeNumber;

	private Socket s; 

	protected static void setServer(UdpServer s) {
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
			
			//Obtener archivo
			File f = UdpVideoUtility.loadVideo();
			
			//Mandar metainfo del video 
			UdpVideoUtility.sendMetaInfo(f, s);

			Thread.sleep(10);
			
			//Mandar info de video
			UdpVideoUtility.sendVideo(new FileInputStream(f), new DatagramSocket(), s.getInetAddress(), 4000);
			
			
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
