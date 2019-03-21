import java.util.Arrays;
import java.util.HashMap;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import com.mongodb.ServerAddress;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;
import java.util.Arrays;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.result.DeleteResult;
import static com.mongodb.client.model.Updates.*;
import com.mongodb.client.result.UpdateResult;
import java.util.ArrayList;
import java.util.List;

public class ConexionBD {	

	private  MongoClientURI uri; 
	private  MongoClient client;
	private  MongoDatabase db;
	private  MongoCollection<Document> usuarios;
	private  MongoCollection<Document> ddos;

	public ConexionBD() {
		uri  = new MongoClientURI("mongodb://federico:admin123@ds117816.mlab.com:17816/laboratorio4"); 
		client = new MongoClient(uri);
		db = client.getDatabase(uri.getDatabase());
		usuarios = db.getCollection("usuarios");
		ddos = db.getCollection("ddos");
	}
	/*
	 * carga una pequeña conlección de usuarios
	 */
	public  void cargarUsuarios() {
		List<Document> seedData = new ArrayList<Document>();

		seedData.add(new Document("user", "federico").append("password", "admin").append("conected", "false")); 
		seedData.add(new Document("user", "juan jose").append("password", "admin").append("conected", "false")); 
		seedData.add(new Document("user", "felipe").append("password", "admin").append("conected", "false")); 
		seedData.add(new Document("user", "juanito").append("password", "admin").append("conected", "false")); 
		seedData.add(new Document("user", "admin").append("password", "admin").append("conected", "false"));         
		usuarios.insertMany(seedData);


	}
	/*
	 * elimina los usuarios
	 */
	public void vaciarUsuarios() {
		usuarios.drop();
	}
	/*
	 * si es invocada boca arriba en modo de ataque
	 * te roba el corazón
	 */
	public  void cerrarBD() {
		client.close();
	}

	/*
	 * crea un nuevo usuario
	 */
	public void crearUsuario(String user, String contraseña) {

		Document nuevo = new Document( "user" ,user).append("password", contraseña);
		usuarios.insertOne(nuevo);

	}
	
	/*
	 * agrega una nueva ip si la lista
	 * de indeceables
	 */
	public void agregarIpDdos(String ip) {

		Document nuevo = new Document( "ip" ,ip);
		ddos.insertOne(nuevo);

	}
	public  boolean ipDdos(String ip) {
		MongoCursor<Document> us = db.getCollection("ddos").find().iterator();

		while(us.hasNext()) {
			Document doc = us.next();
			if(doc.get("ip").equals(ip)) {
					return true;
			}
		}

		return false;

	}

	/*
	 * true si ya se encuentra registrado
	 */
	public  boolean estaRegistrado(String user, String password) {
		MongoCursor<Document> us = db.getCollection("usuarios").find().iterator();

		while(us.hasNext()) {
			Document doc = us.next();
			if(doc.get("user").equals(user)&&doc.get("password").equals(password)) {

				if(doc.get("conected").equals("false")) {
					Document ingreso = new Document("user",user);
					usuarios.updateOne(ingreso, new Document("$set", new Document("conected", "true")));
					return true;
				}
				else {
					return false;
				}
			}
		}

		return false;

	}

	public static void main(String[] args) throws Exception {
		ConexionBD con = new ConexionBD();
		con.vaciarUsuarios();
		con.cargarUsuarios();
		con.cerrarBD();
	}


}
