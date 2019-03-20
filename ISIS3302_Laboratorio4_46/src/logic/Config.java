package logic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Config {

	public static int port;

	public static String videoFile;

	public Config() {
		//Archivo sencillo de configuración debe seguir esquema: 
		//port:<puerto>
		//file:<ruta al video>
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File("config.txt")));
			
			//primera línea corresponde a puerto
			String inLine = bf.readLine();
			port = Integer.parseInt(inLine.split(":")[1]);
			
			//Segunda linea corresponde a ruta del archivo
			inLine = bf.readLine();
			videoFile = inLine.split(":")[1];
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
