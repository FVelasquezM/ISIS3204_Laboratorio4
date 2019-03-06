package srv;

public class ServerThread extends Thread{
	
	private static Server server;
	
	private int activeNumber;
	
	
	protected static void setServer(Server s) {
		server = s;
	}
	
	public ServerThread(int i) {
		//TODO, depende de cada implementación
		System.out.println("Created Thread " + i);
	}

	protected void setActiveNumber(int activeNumber) {
		this.activeNumber = activeNumber;
	}
	
	public void attend() {
		
		//Hacer cosas
		
	}
	
	public void done() {
		//Notificar al servidor que ha acabado, 
		//para así ser puesto en la fila de espera.
		server.threadDone(activeNumber);
		this.activeNumber = -1;
	}
	
	public void run() {
		//OJO, esto debe estar dentro de un while true, pues no es legal que
		//sobre el mismo thread se llame run varias veces.
		//TODO, cómo se maneja que se encuentre dormido????
		//idea: notify y wait.
		attend();
		done();
	}
	
	
}
