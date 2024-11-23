package gui;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private final Usuario usuarioActual;

    public MainMenu(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;

        setTitle("Menú Principal - " + usuarioActual.getTipoUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());
        setResizable(false);

        // Encabezado con bienvenida
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(new Color(4, 52, 84));
        panelEncabezado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblBienvenida = new JLabel(
                "<html><div style='text-align: center;'>Bienvenido, <b>" + usuarioActual.getNombre() + "</b><br>"
                        + "ID de Usuario: <b>" + usuarioActual.getUsuarioID() + "</b></div></html>",
                SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 14));
        lblBienvenida.setForeground(Color.WHITE);

        panelEncabezado.add(lblTitulo, BorderLayout.NORTH);
        panelEncabezado.add(lblBienvenida, BorderLayout.SOUTH);

        // Panel central con opciones
        JPanel panelOpciones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnGestionUsuarios = new JButton("Gestión de Usuarios");
        JButton btnGestionDocumentos = new JButton("Gestión de Documentos");
        JButton btnGestionPrestamos = new JButton("Gestión de Préstamos");
        JButton btnCambiarCodigoAcceso = new JButton("Cambiar Códigos de Acceso");

        btnGestionUsuarios.addActionListener(e -> abrirGestionUsuarios());
        btnGestionDocumentos.addActionListener(e -> abrirGestionDocumentos());
        btnGestionPrestamos.addActionListener(e -> abrirGestionPrestamos());
        btnCambiarCodigoAcceso.addActionListener(e -> cambiarCodigoAcceso());

        // Añadir botones según el tipo de usuario
        if (usuarioActual.getTipoUsuario().equals("Administrador")) {
            panelOpciones.add(btnGestionUsuarios);
            panelOpciones.add(btnGestionDocumentos);
            panelOpciones.add(btnGestionPrestamos);
            panelOpciones.add(btnCambiarCodigoAcceso);
        } else if (usuarioActual.getTipoUsuario().equals("Profesor")) {
            panelOpciones.add(btnGestionDocumentos);
            panelOpciones.add(btnGestionPrestamos);
        } else if (usuarioActual.getTipoUsuario().equals("Alumno")) {
            panelOpciones.add(btnGestionDocumentos);
            panelOpciones.add(btnGestionPrestamos);
        }

        // Botón de cerrar sesión
        JPanel panelCerrarSesion = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(new Color(4, 52, 84)); // Azul específico
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        btnCerrarSesion.setFocusPainted(false);

        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        panelCerrarSesion.add(btnCerrarSesion);
        panelCerrarSesion.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(panelEncabezado, BorderLayout.NORTH);
        add(panelOpciones, BorderLayout.CENTER);
        add(panelCerrarSesion, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void abrirGestionUsuarios() {
        if (usuarioActual.getTipoUsuario().equals("Administrador")) {
            SwingUtilities.invokeLater(() -> new UserManagementView(usuarioActual).setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, "No tiene permisos para acceder a esta opción.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void abrirGestionDocumentos() {
        SwingUtilities.invokeLater(() -> new DocumentManagementView(usuarioActual).setVisible(true));
    }

    private void abrirGestionPrestamos() {
        SwingUtilities.invokeLater(() -> new LoanManagementView(usuarioActual).setVisible(true));
    }

    private void cambiarCodigoAcceso() {
        if (!usuarioActual.getTipoUsuario().equals("Administrador")) {
            JOptionPane.showMessageDialog(this, "Solo los administradores pueden cambiar los códigos de acceso.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nuevoCodigoAdmin = JOptionPane.showInputDialog(this, "Ingrese el nuevo código de acceso para Administrador:");
        String nuevoCodigoProf = JOptionPane.showInputDialog(this, "Ingrese el nuevo código de acceso para Profesor:");

        if (nuevoCodigoAdmin != null && !nuevoCodigoAdmin.isEmpty() &&
                nuevoCodigoProf != null && !nuevoCodigoProf.isEmpty()) {
            RegisterView.actualizarCodigoAcceso(nuevoCodigoAdmin, nuevoCodigoProf);
            JOptionPane.showMessageDialog(this, "Códigos de acceso actualizados con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Los códigos de acceso no pueden estar vacíos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "¿Está seguro de que desea cerrar sesión?", "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
            dispose();
        }
    }
}
