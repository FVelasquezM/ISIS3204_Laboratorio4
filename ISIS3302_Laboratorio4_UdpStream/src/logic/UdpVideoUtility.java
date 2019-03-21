package logic;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.ByteBuffer;

public class UdpVideoUtility {

	public static File loadVideo() {

		try {
			File file = new File("samplewmv.wmv");

			return file;
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		return null;


	}
	
	//Antes de mandar el video, es necesario mandar info: tamaño del archivo
	//a transferirse. Necesario mandarlo por TCP pues NECESITO que llegue.
	public static void sendMetaInfo(File video, Socket s) {
		
		try {
			video.length();
			int packets = (int)(video.length()/1024L)+1;
			PrintWriter out = new PrintWriter(s.getOutputStream(), true);
			System.out.println("ESTIMATED PACKETS " + packets);
			out.write("PKT:" + packets);
			out.flush();
			out.close();
		}
		catch(Exception e) {
			
		}
		
	}
	
	public static void sendVideo(FileInputStream video, DatagramSocket out, InetAddress destination, int destinationPort) throws Exception {


		try {

			int chunckSize = 1024;

			byte[] vBytes = new byte[chunckSize];

			int readBytes = -1;
			DatagramPacket dp; 
			/*String hola = "hola";
			dp = new DatagramPacket(hola.getBytes(), 0, hola.length(), InetAddress.getByName(destination), destinationPort);
			out.send(dp);*/

			byte[] totalBytes;
			int i = 0;
			while((readBytes = video.read(vBytes, 0, chunckSize)) != -1){				
				//Armar nuevo arreglo de bytes. Los dos primeros bytes indican la posici�n del paquete. los dem�s son de datos.

				totalBytes = createTotalBytes(vBytes, readBytes, i);
				//System.out.println(vBytes[0]);
				dp = new DatagramPacket(totalBytes, 0, totalBytes.length, destination, destinationPort);
				out.send(dp);

				/*System.out.println("----------------");
				PrintArray(vBytes);
				System.out.println("VS");
				PrintArray(BreakData(totalBytes));
				*/
				//System.out.println(i);
				//CompareArrays(vBytes, BreakData(totalBytes));
				
				i++;
				Thread.sleep(1);
				
			}

			//mandar un paquete de 2 bytes de longitud con c�digo de finalizaci�n -11;
			byte[] endcode = new byte[4];
			ByteBuffer bb = ByteBuffer.allocate(4).putInt(-1111);
			endcode[0] = bb.get(0);
			endcode[1] = bb.get(1);
			endcode[2] = bb.get(2);
			endcode[3] = bb.get(3);

			dp = new DatagramPacket(endcode, 0, endcode.length, destination, destinationPort);
			out.send(dp);

			System.out.println("PACKETS" + i);

			out.close();
		}
		catch(Exception e) {
			
			if(e.getMessage().equals("mamut")){
				throw e;
			}
			
			e.printStackTrace();
		}

		System.out.println("Video Sent!");

	}
	
	/*private static void CompareArrays(byte[] a, byte[] b) throws Exception{
		
		if(a.length != b.length){
			throw new Exception("mamut");
		}
		System.out.println("LEN: " + a.length);
		for(int i = 0; i<a.length; i++){
			if(a[i] != b[i]){
				//System.out.println("a: " + a[i] + " vs " + b[i] + " i: " + i);
				//PrintArray(a);
				//PrintArray(b);
				throw new Exception("mamut");
			}
		}
	}
	
	private static void PrintArray(byte[] a){
		
		for(int i = 0; i<a.length; i++){
			System.out.print(a[i]+ "|");
		}
		System.out.println();
		
	}*/


	private static byte[] createTotalBytes(byte[] vData, int readBytes, int currentPackage){

		byte[] totalBytes = new byte[readBytes+4];
		ByteBuffer bb = ByteBuffer.allocate(4).putInt(currentPackage);

		totalBytes[0] = bb.get(0);
		totalBytes[1] = bb.get(1);
		totalBytes[2] = bb.get(2);
		totalBytes[3] = bb.get(3);

		//Dos primeros bytes indican posici�n.
		for(int i = 4; i< totalBytes.length; i++){
			totalBytes[i] = vData[i-4];
			//System.out.println("i: "+ i + " | " + vData[i-4] + " | " + totalBytes[i]);
		}
		
		//System.out.println("TOTAL BYTES");
		
		//PrintArray(totalBytes);


		return totalBytes;

	}

}
