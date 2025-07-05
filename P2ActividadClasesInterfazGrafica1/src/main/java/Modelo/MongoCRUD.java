package Modelo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import com.mongodb.client.model.Filters;
import java.util.ArrayList;
import java.util.List;

public class MongoCRUD {
    // Crear (Insertar)
    public void Crear(Equipo equipo) {
        ConexionBD conexion = new ConexionBD();
        MongoCollection<Document> coleccion = conexion.getEquiposCollection();
        Document doc = new Document("codigo", equipo.getCodigo())
                .append("nombre", equipo.getNombre())
                .append("categoria", equipo.getCategoria())
                .append("estado", equipo.getEstado())
                .append("tieneAccesorio", equipo.isTieneAccesorio())
                .append("accesorio", equipo.getAccesorio());
        coleccion.insertOne(doc);
        conexion.cerrar();
    }

    // Leer (Buscar por codigo)
    public Equipo Leer(String codigo) {
        ConexionBD conexion = new ConexionBD();
        MongoCollection<Document> coleccion = conexion.getEquiposCollection();
        Document doc = coleccion.find(Filters.eq("codigo", codigo)).first();
        conexion.cerrar();
        if (doc != null) {
            return new Equipo(
                doc.getString("codigo"),
                doc.getString("nombre"),
                doc.getString("categoria"),
                doc.getString("estado"),
                doc.getBoolean("tieneAccesorio", false),
                doc.getString("accesorio")
            );
        }
        return null;
    }

    // Actualizar
    public void Actualizar(String codigo, Equipo equipoActualizado) {
        ConexionBD conexion = new ConexionBD();
        MongoCollection<Document> coleccion = conexion.getEquiposCollection();
        Document actualizacion = new Document("$set",
            new Document("nombre", equipoActualizado.getNombre())
                .append("categoria", equipoActualizado.getCategoria())
                .append("estado", equipoActualizado.getEstado())
                .append("tieneAccesorio", equipoActualizado.isTieneAccesorio())
                .append("accesorio", equipoActualizado.getAccesorio())
        );
        coleccion.updateOne(Filters.eq("codigo", codigo), actualizacion);
        conexion.cerrar();
    }

    // Eliminar
    public void Eliminar(String codigo) {
        ConexionBD conexion = new ConexionBD();
        MongoCollection<Document> coleccion = conexion.getEquiposCollection();
        coleccion.deleteOne(Filters.eq("codigo", codigo));
        conexion.cerrar();
    }

    // Listar todos los equipos
    public List<Equipo> listarTodos() {
        List<Equipo> lista = new ArrayList<>();
        ConexionBD conexion = new ConexionBD();
        MongoCollection<Document> coleccion = conexion.getEquiposCollection();
        FindIterable<Document> docs = coleccion.find();
        for (Document doc : docs) {
            Equipo equipo = new Equipo(
                doc.getString("codigo"),
                doc.getString("nombre"),
                doc.getString("categoria"),
                doc.getString("estado"),
                doc.getBoolean("tieneAccesorio", false),
                doc.getString("accesorio")
            );
            lista.add(equipo);
        }
        conexion.cerrar();
        return lista;
    }
}
