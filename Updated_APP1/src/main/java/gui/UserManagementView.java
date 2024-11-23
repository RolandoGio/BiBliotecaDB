package gui;

import DAO.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserManagementView extends JFrame {
    private final UsuarioDAO usuarioDAO;
    private JTable table;
    private DefaultTableModel tableModel;

    public UserManagementView(Usuario usuarioActual) {
        usuarioDAO = new UsuarioDAO();


        setTitle("Gestión de Usuarios");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // Tabla de usuarios
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        String[] columnNames = {"ID", "Nombre", "Tipo"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        cargarUsuarios();
        panelTabla.add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar Usuario");
        JButton btnEditar = new JButton("Editar Usuario");
        JButton btnEliminar = new JButton("Eliminar Usuario");

        btnAgregar.addActionListener(e -> agregarUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnEliminar.addActionListener(e -> eliminarUsuario());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarUsuarios("Administrador");
            tableModel.setRowCount(0);
            for (Usuario usuario : usuarios) {
                tableModel.addRow(new Object[]{
                        usuario.getUsuarioID(),
                        usuario.getNombre(),
                        usuario.getTipoUsuario(),
                        usuario.getContrasena()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void agregarUsuario() {
        String nombre = JOptionPane.showInputDialog(this, "Ingrese el nombre del usuario:");
        String tipo = (String) JOptionPane.showInputDialog(this,
                "Seleccione el tipo de usuario:",
                "Tipo de Usuario",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Alumno", "Profesor", "Administrador"},
                "Alumno");
        String contrasena = JOptionPane.showInputDialog(this, "Ingrese la contraseña:");

        if (nombre != null && tipo != null && contrasena != null) {
            try {
                Usuario nuevoUsuario = new Usuario(null, nombre, tipo, contrasena);
                usuarioDAO.agregarUsuario(nuevoUsuario);
                cargarUsuarios();
                JOptionPane.showMessageDialog(this, "Usuario agregado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al agregar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarUsuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String usuarioID = String.valueOf(tableModel.getValueAt(selectedRow, 0));
            String nombreActual = String.valueOf(tableModel.getValueAt(selectedRow, 1));
            String tipoActual = String.valueOf(tableModel.getValueAt(selectedRow, 2));

            String nuevoNombre = JOptionPane.showInputDialog(this, "Nuevo nombre:", nombreActual);
            String nuevoTipo = (String) JOptionPane.showInputDialog(this,
                    "Seleccione el nuevo tipo de usuario:",
                    "Tipo de Usuario",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Alumno", "Profesor", "Administrador"},
                    tipoActual);
            String nuevaContrasena = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña:");

            if (nuevoNombre != null && nuevoTipo != null && nuevaContrasena != null) {
                try {
                    Usuario usuarioEditado = new Usuario(usuarioID, nuevoNombre, nuevoTipo, nuevaContrasena);
                    usuarioDAO.actualizarUsuario(usuarioEditado);
                    cargarUsuarios();
                    JOptionPane.showMessageDialog(this, "Usuario actualizado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al actualizar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarUsuario() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String usuarioID = String.valueOf(tableModel.getValueAt(selectedRow, 0));
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    usuarioDAO.eliminarUsuario(usuarioID);
                    cargarUsuarios();
                    JOptionPane.showMessageDialog(this, "Usuario eliminado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
