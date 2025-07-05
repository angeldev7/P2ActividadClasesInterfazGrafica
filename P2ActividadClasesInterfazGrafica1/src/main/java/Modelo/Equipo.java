package Modelo;

public class Equipo {
    private String codigo;
    private String nombre;
    private String categoria; // Laptop, Proyector, etc.
    private String estado;    // Operativo, En reparacion, Dado de baja
    private boolean tieneAccesorio; // true si el checkbox está activado
    private String accesorio; // Mochila, Mouse, USB (solo uno)

    public Equipo(String codigo, String nombre, String categoria, String estado, boolean tieneAccesorio, String accesorio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.estado = estado;
        this.tieneAccesorio = tieneAccesorio;
        this.accesorio = accesorio;
    }

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    public String getEstado() {
        return estado;
    }
    public void setEstado(String estado) {
        this.estado = estado;
    }
    public boolean isTieneAccesorio() {
        return tieneAccesorio;
    }
    public void setTieneAccesorio(boolean tieneAccesorio) {
        this.tieneAccesorio = tieneAccesorio;
    }
    public String getAccesorio() {
        return accesorio;
    }
    public void setAccesorio(String accesorio) {
        this.accesorio = accesorio;
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", estado='" + estado + '\'' +
                ", tieneAccesorio=" + tieneAccesorio +
                ", accesorio='" + accesorio + '\'' +
                '}';
    }
}
