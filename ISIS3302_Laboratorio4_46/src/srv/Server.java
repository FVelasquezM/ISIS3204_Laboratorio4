package srv;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

//Clase singleton.
public class Server {

	private static Server instance = null;

	private final int THREAD_COUNT = 150;

	private ArrayList<ServerThread> waitingThreads;

	private ArrayList<ServerThread> activeThreads;

	//Queue de clientes que no han sido atendidos porque no había 
	//threads disponibles en el pool cuando llegaron
	private  ArrayList<Socket> clientQueue;

	private Server(){

		waitingThreads = new ArrayList<ServerThread>();
		activeThreads = new ArrayList<ServerThread>();

		clientQueue = new ArrayList<Socket>();

		initializeThreads();
		
		ServerThread.setServer(this);
		
		try {
			ServerSocket ss = new ServerSocket(2222);
			acceptConns(ss);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static Server getInstance() {
		return instance;
	}

	public static void instantiate() throws Exception{

		if(instance == null) {
			instance = new Server();
		}
		else {
			throw new Exception("Server singleton already initialized");
		}

	}

	/**
	 * Pre: ss != null
	 * @param ss
	 */
	private void acceptConns(ServerSocket ss) {

		try {
			Socket c;
			System.out.println("Ready to accept connections");
			while(true) {
				c = ss.accept();
				System.out.println("Connection received");
				if(waitingThreads.isEmpty()) {
					//Todos los threads ocupados, poner en espera.
					clientQueue.add(c);

					//TODO, método para registrar en un archivo la cantidad de personas
					//que están en espera. Idealmente con un thread para no 
					//ralentizar mucho la ejeucución de la parte del servidor que 
					//recibe conexiones.

				}
				else {
					ServerThread st = waitingThreads.remove(waitingThreads.size()-1);
					assignThread(st, c);
				}
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void assignThread(ServerThread st, Socket c) {

		//Hay un thread disponible, atender.
		activeThreads.add(st);
		System.out.println("assigning thread");
		synchronized(st) {
			st.setActiveNumber(activeThreads.size()-1);
			st.setSocket(c);
			st.notify();
			System.out.println("Thread notified");
		}

	}


	/**
	 * Pre: waitingThreads != null
	 */
	private void initializeThreads() {

		ServerThread st;
		for(int i = 0; i<THREAD_COUNT; i++) {
			st = new ServerThread(i);
			st.start();
			waitingThreads.add(st);
		}

	}

	protected synchronized void threadDone(int activeNum) {

		//El thread ha terminado, sacarlo de activeThreads 
		//Si ha clientes en espera, que atienda a cliente.
		//De modo contrario, pasar a waitingThreads
		ServerThread st = activeThreads.remove(activeNum);
		
		if(clientQueue.isEmpty()) {
			//No hay clientes en espera, pasar thread a estado de espera.
			//pasar thread a espera. 
			waitingThreads.add(st);
		}
		else {
			//Hay clientes en espera, asignar inmediatamente.
			assignThread(st, clientQueue.remove(0));

		}
	}

	public static void main(String args[]) {

		try {
			Server.instantiate();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
