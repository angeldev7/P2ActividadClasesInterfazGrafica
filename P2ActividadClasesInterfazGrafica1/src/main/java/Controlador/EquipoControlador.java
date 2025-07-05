package Controlador;
import Modelo.Equipo;
import Modelo.MongoCRUD;
import Modelo.ConexionBD;
import Vista.Inventario;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EquipoControlador implements ActionListener {
    private Inventario vista;
    private MongoCRUD crud;

    public EquipoControlador(Inventario vista) {
        this.vista = vista;
        this.crud = new MongoCRUD();
        // Asignar listeners a los botones
        this.vista.BtnGuardar.addActionListener(this);
        this.vista.BtnEliminar.addActionListener(this);
        this.vista.BtnCancelar.addActionListener(this);
        this.vista.BtnBuscarProducto.addActionListener(this);
        this.vista.BtnEliminarBD.addActionListener(this);
        this.vista.BtnEditar.addActionListener(this);
        this.vista.BtnVaciarInventario.addActionListener(this);
        // Agregar listener para seleccionar fila de la tabla
        this.vista.TablaInventario.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vista.TablaInventario.getSelectedRow() != -1) {
                int fila = vista.TablaInventario.getSelectedRow();
                vista.TxtCodigo.setText(vista.TablaInventario.getValueAt(fila, 1).toString());
                vista.TxtNombre.setText(vista.TablaInventario.getValueAt(fila, 2).toString());
                vista.CmboxCategoria.setSelectedItem(vista.TablaInventario.getValueAt(fila, 3).toString());
                String estado = vista.TablaInventario.getValueAt(fila, 4).toString();
                if (estado.equals("Operativo")) vista.rBtnFuncional.setSelected(true);
                else if (estado.equals("En reparacion")) vista.rBtnReparacion.setSelected(true);
                else if (estado.equals("Dado de baja")) vista.rBtnDeBaja.setSelected(true);
                String accesorio = vista.TablaInventario.getValueAt(fila, 5).toString();
                if (!accesorio.equals("Ninguno")) {
                    vista.CheckAccesorios.setSelected(true);
                    vista.CmBoxAccesorios.setSelectedItem(accesorio);
                    vista.CmBoxAccesorios.setVisible(true);
                } else {
                    vista.CheckAccesorios.setSelected(false);
                    vista.CmBoxAccesorios.setSelectedIndex(0);
                    vista.CmBoxAccesorios.setVisible(false);
                }
            }
        });
        // Listener para cerrar el programa
        this.vista.BtnCerrar.addActionListener(e -> {
            System.exit(0);
        });
        // Inicialmente ocultar el ComboBox de accesorios
        this.vista.CmBoxAccesorios.setVisible(false);
        // Mostrar u ocultar ComboBox de accesorios según el estado del CheckBox
        this.vista.CheckAccesorios.addActionListener(e -> {
            boolean seleccionado = this.vista.CheckAccesorios.isSelected();
            this.vista.CmBoxAccesorios.setVisible(seleccionado);
        });
    }

    // Separar logica de validacion y mensajes
    private boolean camposObligatoriosLlenos(String codigo, String nombre, String categoria, String estado) {
        return !(codigo.isEmpty() || nombre.isEmpty() || categoria.isEmpty() || estado.isEmpty());
    }

    private void mostrarMensaje(String mensaje) {
        javax.swing.JOptionPane.showMessageDialog(vista, mensaje);
    }

    // Metodo reutilizable para llenar la tabla con una lista de equipos
    private void mostrarEquiposEnTabla(java.util.List<Equipo> lista) {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.TablaInventario.getModel();
        modelo.setRowCount(0);
        int i = 1;
        for (Equipo eq : lista) {
            modelo.addRow(new Object[] {
                i++,
                eq.getCodigo(),
                eq.getNombre(),
                eq.getCategoria(),
                eq.getEstado(),
                eq.isTieneAccesorio() ? eq.getAccesorio() : "Ninguno"
            });
        }
    }

    // Nombres mas claros para metodos
    private void buscarEquipoPorCodigo() {
        String codigo = vista.TxtBuscar.getText();
        if (codigo.isEmpty()) {
            mostrarMensaje("Ingrese el codigo a buscar.");
            return;
        }
        Equipo equipo = crud.Leer(codigo);
        java.util.List<Equipo> lista = new java.util.ArrayList<>();
        if (equipo != null) {
            lista.add(equipo);
            mostrarEquiposEnTabla(lista);
        } else {
            mostrarMensaje("Equipo no encontrado.");
            mostrarEquiposEnTabla(lista); // Muestra tabla vacia
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.BtnGuardar) {
            guardarEquipo();
        } else if (e.getSource() == vista.BtnEliminar) {
            eliminarEquipo();
        } else if (e.getSource() == vista.BtnCancelar) {
            limpiarCampos();
        } else if (e.getSource() == vista.BtnBuscarProducto) {
            buscarEquipoPorCodigo();
        } else if (e.getSource() == vista.BtnEliminarBD) {
            eliminarBD();
        } else if (e.getSource() == vista.BtnEditar) {
            editarEquipo();
        }
        else if (e.getSource() == vista.BtnVaciarInventario) {
    vaciarInventario();
        }
    }

    // Metodo para listar todos los equipos y llenar la tabla
    private void refrescarTabla() {
        mostrarEquiposEnTabla(crud.listarTodos());
    }

    private void guardarEquipo() {
        String codigo = vista.TxtCodigo.getText();
        String nombre = vista.TxtNombre.getText();
        String categoria = vista.CmboxCategoria.getSelectedItem().toString();
        String estado = vista.rBtnFuncional.isSelected() ? "Operativo" :
                        vista.rBtnReparacion.isSelected() ? "En reparacion" :
                        vista.rBtnDeBaja.isSelected() ? "Dado de baja" : "";
        boolean tieneAccesorio = vista.CheckAccesorios.isSelected();
        String accesorio = tieneAccesorio ? vista.CmBoxAccesorios.getSelectedItem().toString() : "Ninguno";
        if (!camposObligatoriosLlenos(codigo, nombre, categoria, estado)) {
            mostrarMensaje("Todos los campos obligatorios deben estar llenos.");
            return;
        }
        Equipo existente = crud.Leer(codigo);
        if (existente != null) {
            mostrarMensaje("Ya existe un equipo con ese codigo. Usa otro codigo unico.");
            return;
        }
        Equipo equipo = new Equipo(codigo, nombre, categoria, estado, tieneAccesorio, accesorio);
        crud.Crear(equipo);
        mostrarMensaje("Equipo guardado correctamente.");
        limpiarCampos();
        refrescarTabla();
        vista.CmBoxAccesorios.setVisible(false);
    }

    private void eliminarEquipo() {
        String codigo = vista.TxtBuscar.getText();
        if (codigo.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(vista, "Ingrese el codigo del equipo a eliminar.");
            return;
        }
        crud.Eliminar(codigo);
        javax.swing.JOptionPane.showMessageDialog(vista, "Equipo eliminado.");
        limpiarCampos();
        refrescarTabla();
        vista.CmBoxAccesorios.setVisible(false);
    }

    private void limpiarCampos() {
        vista.TxtCodigo.setText("");
        vista.TxtNombre.setText("");
        vista.CmboxCategoria.setSelectedIndex(0);
        vista.GrupoBotones.clearSelection();
        vista.CheckAccesorios.setSelected(false);
        vista.CmBoxAccesorios.setSelectedIndex(0);
        vista.CmBoxAccesorios.setVisible(false);
        vista.TxtBuscar.setText("");
    }

    private void eliminarBD() {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(vista, "Seguro que deseas eliminar toda la base de datos?", "Confirmar", javax.swing.JOptionPane.YES_NO_OPTION);
        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
            ConexionBD conexion = new ConexionBD();
            var coleccion = conexion.getEquiposCollection();
            coleccion.drop();
            conexion.cerrar();
            javax.swing.JOptionPane.showMessageDialog(vista, "Base de datos eliminada.");
            limpiarCampos();
            refrescarTabla();
            vista.CmBoxAccesorios.setVisible(false);
        }
    }

    // Validacion de nombre (ahora permite letras, numeros y espacios, minimo 3 caracteres)
    private boolean nombreValido(String nombre) {
        return nombre.matches("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ ]{3,}");
    }

    private void editarEquipo() {
        String codigo = vista.TxtCodigo.getText();
        String nombre = vista.TxtNombre.getText();
        String categoria = vista.CmboxCategoria.getSelectedItem().toString();
        String estado = vista.rBtnFuncional.isSelected() ? "Operativo" :
                        vista.rBtnReparacion.isSelected() ? "En reparacion" :
                        vista.rBtnDeBaja.isSelected() ? "Dado de baja" : "";
        boolean tieneAccesorio = vista.CheckAccesorios.isSelected();
        String accesorio = tieneAccesorio ? vista.CmBoxAccesorios.getSelectedItem().toString() : "Ninguno";
        if (!camposObligatoriosLlenos(codigo, nombre, categoria, estado)) {
            mostrarMensaje("Todos los campos obligatorios deben estar llenos.");
            return;
        }
        if (!nombreValido(nombre)) {
            mostrarMensaje("El nombre debe tener al menos 3 letras y solo caracteres alfabéticos.");
            return;
        }
        Equipo existente = crud.Leer(codigo);
        if (existente == null) {
            mostrarMensaje("No existe un equipo con ese código para editar.");
            return;
        }
        Equipo equipoActualizado = new Equipo(codigo, nombre, categoria, estado, tieneAccesorio, accesorio);
        crud.Actualizar(codigo, equipoActualizado);
        mostrarMensaje("Equipo actualizado correctamente.");
        limpiarCampos();
        refrescarTabla();
        vista.CmBoxAccesorios.setVisible(false);
    }

    // Metodo para vaciar inventario (solo limpiar la tabla visualmente, no la base de datos)
    private void vaciarInventario() {
        javax.swing.table.DefaultTableModel modelo = (javax.swing.table.DefaultTableModel) vista.TablaInventario.getModel();
        modelo.setRowCount(0); // Limpia la tabla visualmente
        vista.TablaInventario.clearSelection(); // Limpia la selección de la tabla
        limpiarCampos(); // Limpia los campos del formulario
    }

    // Llama a refrescarTabla al iniciar el controlador
    public void iniciar() {
        refrescarTabla();
    }
}
