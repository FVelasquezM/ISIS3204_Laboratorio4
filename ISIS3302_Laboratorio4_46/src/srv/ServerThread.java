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
		
		try {
			this.wait();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	protected void setActiveNumber(int activeNumber) {
		this.activeNumber = activeNumber;
	}
	
	
	protected void attend() {
		
		//Hacer cosas, según implementación.
		
	}
	
	
	public void run() {
		//OJO, esto debe estar dentro de un while true, pues no es legal que
		//sobre el mismo thread se llame run varias veces.
		//TODO, cómo se maneja que se encuentre dormido????
		//idea: notify y wait.
		
		//Thread inicia dormido. Cuando es notificado del éxito de
		//
		try {
			this.wait();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		while(true) {
			
			attend();
			
			
			//Notificar al servidor que ha acabado, 
			//para así ser puesto en la fila de espera.
			server.threadDone(activeNumber);
			this.activeNumber = -1;
			
			//Dormirse, se debe ahora esperar a que el servidor
			//reinicie la ejecución.
			try {
				this.wait();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
