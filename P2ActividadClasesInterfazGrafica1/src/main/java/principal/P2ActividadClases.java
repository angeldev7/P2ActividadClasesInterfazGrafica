package principal;
import Controlador.EquipoControlador;
import Vista.Inventario;
public class P2ActividadClases {
public static void main(String[] args) {
    Inventario vista = new Inventario();
    EquipoControlador controlador = new EquipoControlador(vista);
    vista.setVisible(true);
    controlador.iniciar(); // Para que la tabla se llene al inicio
}
}
