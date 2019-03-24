import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;


import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;

import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class Server  {

	private ServerSocket server;
	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private ConexionBD db;

	public Server(String ipAddress) throws Exception {
		if (ipAddress != null && !ipAddress.isEmpty()) 
			this.server = new ServerSocket(420);
		else 
			this.server = new ServerSocket(420);
		db = new ConexionBD();
		
	}


	private void inicio() throws IOException {
		while (true)  
		{ 
			Socket s = null; 

			try 
			{ 
				// socket object to receive incoming client requests 
				s = this.server.accept(); 

				String clientAddress = s.getInetAddress().getHostAddress();
				if(!db.ipDdos(clientAddress)) {
					
				
				System.out.println("\r\nNew connection from " + clientAddress);

				// obtaining input and out streams 
				BufferedReader dis = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter dos = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

				System.out.println("Assigning new thread for this client"); 

				// create a new thread object 
				Thread t = new ServerThread(s, dis, dos); 

				// Invoking the start() method 
				t.start(); 
				System.out.println("se inició el thread");
				}else {
					s.close();
				}

			} 
			catch (Exception e){ 
				s.close(); 
				e.printStackTrace(); 
			} 
		} 
	}


	public InetAddress getSocketAddress() {
		return this.server.getInetAddress();
	}

	public int getPort() {
		return this.server.getLocalPort();
	}
	public static void main(String[] args) throws Exception {
		Server app = new Server(args[0]);
		System.out.println("\r\nRunning Server: " + 
				"Host=" + app.getSocketAddress().getHostAddress() + 
				" Port=" + app.getPort());

		app.inicio();
	}
}