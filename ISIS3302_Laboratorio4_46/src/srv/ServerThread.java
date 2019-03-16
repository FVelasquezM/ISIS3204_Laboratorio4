package srv;

import java.io.PrintWriter;
import java.net.Socket;

import javax.net.ssl.SSLContext;

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
		
		//Hacer cosas, según implementación.
		try {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			out.print("Thread " + threadNumber + " active");
			out.close();
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
