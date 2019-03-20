import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
public class Cliente {
	
    private Socket socket;
    private Scanner scanner;
    private BufferedReader read;
    private PrintWriter out;
    private Cliente(InetAddress serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        this.scanner = new Scanner(System.in);
    }
    private void start() throws IOException {
        
        String input;
        read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream(), true);
        System.out.println(read.readLine());
        for(int i=0; i<3; i++) {
        	 System.out.println("Enter User: ");
             input = scanner.nextLine();
             out.println(input);
             System.out.println("enter Password: ");
             input =scanner.nextLine();
             out.println(input);
             System.out.println("Server is proccesing... ");
             String response = read.readLine();
             System.out.println(response);
             if(!"Login Failed".equals(response)) {
            	 
            	 break;
             }
             
        }
        
        out.flush();
        out.close();
        
      
    }
    public static void main(String[] args) throws Exception {
        Cliente client = new Cliente(
                InetAddress.getByName(args[0]), 
                Integer.parseInt(args[1]));
        
        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();                
    }
    
    
}