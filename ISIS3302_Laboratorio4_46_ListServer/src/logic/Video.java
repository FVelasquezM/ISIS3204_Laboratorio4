package logic;

/**
 * Representa la información de un video
 * @author felipe
 *
 */
public class Video {

	private String name; 
	
	private String ip;
	
	private int port;
	
	public Video(String name, String ip, int port) {
		this.name = name; 
		this.ip = ip;
		this.port = port;
	}
	
	//Retorna el video siguiendo el protocolo(VID:<NAME>:<IP>:<PORT>
	public String toString() {
		return "VID:"+name+":"+ip+":"+port;
	}
	
}
