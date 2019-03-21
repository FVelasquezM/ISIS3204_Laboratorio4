package view;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class UdpVideoPlayer {

	//Arreglo que contiene los paquetes recibidos ordenados seg�n lo indica el paquete.
	public static ArrayList<byte[]> data;

	private static int lastWritten = 0;

	private static boolean first = true;

	private static Process p;
	
	private  static int expectedPackets;
	
	private  static int playThreshold;
	
	public static void play(String host, int port){

		//Primero, recibir meta inf por un socket, número de paquetes
		expectedPackets = getPacketNumber(host, port);
		
		//Número de paquetes dividido 4, redondeado al múltiplo de 1024 más cercano
		for(int i = expectedPackets/4; i<expectedPackets; i++) {
			if(i % 1024 == 0) {
				playThreshold = i; 
				break;
			}
		}

		data = new ArrayList<>(expectedPackets+200);

		for(int i = 0; i< expectedPackets+200; i++){
			data.add(i, new byte[0]);
		}
		System.out.println(data.size());



		try{


			File f = new File("udptransm.wmv");

			FileOutputStream fo = new FileOutputStream(f);

			byte[] inBytes = new byte[1028];

			DatagramSocket dsoc=new DatagramSocket(4000);
			DatagramPacket dp;
			int pos;
			byte[] receivedData;
			System.out.println("listening...");
			while(true){
				dp = new DatagramPacket(inBytes,inBytes.length);
				dsoc.receive(dp);
				//System.out.println(new String(dp.getData(),0,dp.getLength()));

				receivedData = dp.getData();

				if(receivedData.length != 1028){
					System.out.println("MAMUT " + receivedData.length);
					return;
				}

				//Leer cautro primeros bytes, corresponden a orden.
				pos = getPos(receivedData);
				//System.out.println("pakcet: " + pos);
				//Si pos == -11, se ha acabado la transmisi�n


				if(pos == -1111){
					System.out.println("BREAK");
					break;
				}

				addToData(breakData(receivedData), pos);
				writePartial(fo);
			}

			System.out.println("Received data");


			fo.close();

			//Esperar a que el cliente se salga del reproductor 
			//para eliminar el archivo.
			p.waitFor();
			f.delete();
			dsoc.close();
		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

	private static int getPacketNumber(String host, int port) {

		try {
			Socket s = new Socket(host, port);
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
			String[] parts = in.readLine().split(":");
			System.out.println("Received meta inf");
			return Integer.parseInt(parts[1]);
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	private static boolean solveMissingPackage(int m, FileOutputStream fo) throws IOException {

		if(m == 0 ) {
			return false;
		}
		//Si ya llegaron al menos cinco paquetes posteriores
		int count = 0; 	
		for(int i = m+1; i < data.size(); i++) {
			if(data.get(i).length != 0 ) {
				count ++;
			}
			if(count == 5) {
				byte[] b = new byte[1024];
				data.set(m, b);
				fo.write(b, 0, b.length);
				System.out.println("written missing package " + m);
				return true;
			}
		}
		return false;

	}

	private static void writePartial(FileOutputStream fo) {

		//Recorre desde lastwritten hasta último paquete recibido en orden, escribe estos paquetes.
		try {
			int i = lastWritten;
			byte[] b;
			for(; i < data.size(); i++) {
				b = data.get(i);
				if(b.length == 0) {
					if(!solveMissingPackage(i, fo)) {
						break;
					}
				}
				fo.write(b,0,b.length);
				System.out.println("Written up to " + i);
				//OJO i debe ser múltiplo de 1024 para que se continue la carga del
				//video adecuadamente (wtf). 
				
				
				if(first && i>= playThreshold) {
					System.out.println("Opening VLC");
					ProcessBuilder pb = new ProcessBuilder("/usr/bin/vlc", "udptransm.wmv", "-L");
					p = pb.start();
					first = false;
				}
			}
			lastWritten = i;
		}
		catch(Exception e) {
			e.printStackTrace();
		}



	}

	/*private static Byte[] toByteArray(){


		ArrayList<Byte> bytes = new ArrayList<Byte>();
		for(int i = 0; i < data.size(); i++){

			if(data.get(i).length == 0){
				break;
			}
			else{

				for(int j = 0; j<data.get(i).length; j++){
					bytes.add(data.get(i)[j]);
				}

			}

		}
		Byte[] bytesarray = new Byte[bytes.size()];
		bytes.toArray(bytesarray);
		return bytesarray;
	}*/

	private static void addToData(byte[] toAdd, int pos){

		if (data.size() < pos +1){
			System.out.println("C en " + pos);
			data.ensureCapacity(pos+1);
			System.out.println("new c" + data.size());
		}
		data.set(pos, toAdd);

	}

	private static int getPos(byte[] receivedData){
		byte[] intB = new byte[4];

		for(int i = 0; i<4; i++){
			intB[i] = receivedData[i];
		}

		return ByteBuffer.wrap(intB).getInt();

	}


	//Devuelve arreglo de bytes sin los dos bytes de control
	private static byte[] breakData(byte[] receivedData){

		byte[] ret = new byte[receivedData.length-4];

		for(int i = 4; i<receivedData.length; i++){
			ret[i-4] = receivedData[i];
		}

		return ret;

	}

}
