import java.net.ServerSocket;
import java.net.Socket;

public class UploaderMain {

	public static void main(String[] args) {

		try {
			
			ServerSocket ss = new ServerSocket(4321);
			
			Socket s; 
			while(true) {
				s = ss.accept();
				UploaderProtocolListener.ListenForUpload(s);
			}
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}


	}

}
