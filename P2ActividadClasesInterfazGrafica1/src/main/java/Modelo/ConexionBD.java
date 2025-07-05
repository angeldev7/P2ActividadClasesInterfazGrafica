package Modelo;
import javax.swing.JOptionPane;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class ConexionBD {

    public static final String URI = "mongodb://localhost:27017"; 
    public static final String NOMBRE_BD = "inventarioBD"; // Nombre de la base de datos
    public MongoClient mongoClient;
    public MongoDatabase database;
    //constructor que inicializa la conexión a la base de datos
    public void conectar() {
        try {
            mongoClient = MongoClients.create(URI);
            database = mongoClient.getDatabase(NOMBRE_BD);
        } catch (Exception e) {
           javax.swing.JOptionPane.showMessageDialog(null, "Error al conectar a la base datos" +e.toString()); 
        }
    }
    //metodo para obtener la coleccion de equuipos
    public MongoCollection<Document> getEquiposCollection() {
        if (database == null) {
            conectar(); // Asegurarse de que la conexio este establecida
        }
        return database.getCollection("equipos"); // Nombre de la coleccion
    }
    //metodo para cerrar la conexion a la base de datos
    public void cerrar() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}
