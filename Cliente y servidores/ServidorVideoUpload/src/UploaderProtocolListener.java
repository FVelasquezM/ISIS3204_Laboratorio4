import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class UploaderProtocolListener {

	/**
	 * Espera a que el cliente indique que va a subir un video, después 
	 * redirige a uploader.
	 */
	public static void ListenForUpload(Socket s) {

		try {

			PrintWriter out = new PrintWriter(s.getOutputStream(),true);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			
		   
			Uploader.GetUpload(s,System.currentTimeMillis()+ ".wmv");
			
			//Escribe DON para indicarle al cliente que ha subido el archivo.
			out.println("received successfully");
			
			System.out.println("received successfully");
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
