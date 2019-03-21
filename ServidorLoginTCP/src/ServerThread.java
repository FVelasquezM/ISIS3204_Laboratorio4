import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerThread extends Thread{

	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private ConexionBD bd;

	public ServerThread(Socket s, BufferedReader dis,PrintWriter dos ) {
		this.out = dos;
		this.in = dis;
		this.client = s;
		bd = new ConexionBD();


	}

	public void run() {
		try {
			listen();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void listen() throws Exception {
		String clientAddress = client.getInetAddress().getHostAddress();
		System.out.println("\r\nNew connection from " + clientAddress);

		in = new BufferedReader(new InputStreamReader(client.getInputStream()));  
		out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true );
        out.println("hola");
		for(int i=0; i<3; i++) {
			String username = in.readLine();
			System.out.println("username: " + username);
			String password = in.readLine();
			System.out.println("password: " + password);

			if(bd.estaRegistrado(username, password)){
				System.out.println("Welcome, " + username);
				out.println("Welcome, " + username);
				break;
			}else{
				out.println("Login Failed");
				System.out.println("Login Failed");
			}
			if(i==2) {
				bd.agregarIpDdos(clientAddress);
			}
		}

		bd.cerrarBD();
		out.flush();
		out.close();
	}

}
