package gui;

import DAO.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private final UsuarioDAO usuarioDAO;
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbTipo;
    private JPasswordField txtCodigoAcceso;

    // Códigos de acceso iniciales
    private static String codigoAccesoAdmin = "ADMINCDB";
    private static String codigoAccesoProf = "PROFCDB";

    public RegisterView() {
        usuarioDAO = new UsuarioDAO();

        setTitle("Sistema de Biblioteca - Registro de Usuario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 400);
        setLayout(new BorderLayout());
        setResizable(false);

        // Encabezado
        JPanel panelEncabezado = new JPanel(new FlowLayout());
        panelEncabezado.setBackground(new Color(4, 52, 84));
        JLabel lblTitulo = new JLabel("Registro de Usuario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo);

        // Formulario de Registro
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        txtNombre = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField();
        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        cmbTipo = new JComboBox<>(new String[]{"Alumno", "Profesor", "Administrador"});
        JLabel lblCodigoAcceso = new JLabel("Código de Acceso:");
        txtCodigoAcceso = new JPasswordField();

        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(lblTipo);
        panelFormulario.add(cmbTipo);
        panelFormulario.add(lblCodigoAcceso);
        panelFormulario.add(txtCodigoAcceso);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVolver = new JButton("Volver");
        btnRegistrar.setBackground(new Color(4, 52, 84));
        btnRegistrar.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(4, 52, 84));
        btnVolver.setForeground(Color.WHITE);

        btnRegistrar.addActionListener(e -> registrarUsuario());
        btnVolver.addActionListener(e -> volverAlLogin());

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnVolver);

        // Añadir componentes al JFrame
        add(panelEncabezado, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void registrarUsuario() {
        String nombre = txtNombre.getText().trim();
        String contrasena = new String(txtPassword.getPassword());
        String tipo = (String) cmbTipo.getSelectedItem();
        String codigoAcceso = new String(txtCodigoAcceso.getPassword());

        // Validación de campos
        if (nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar código de acceso para Administrador y Profesor
        if (tipo.equals("Administrador") && !codigoAcceso.equals(codigoAccesoAdmin)) {
            JOptionPane.showMessageDialog(this, "Código de acceso incorrecto para Administrador.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (tipo.equals("Profesor") && !codigoAcceso.equals(codigoAccesoProf)) {
            JOptionPane.showMessageDialog(this, "Código de acceso incorrecto para Profesor.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario nuevoUsuario = new Usuario(null, nombre, tipo, contrasena);
            usuarioDAO.agregarUsuario(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volverAlLogin();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void volverAlLogin() {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
        dispose();
    }

    // Método para actualizar códigos de acceso (Accesible desde MainMenu)
    public static void actualizarCodigoAcceso(String nuevoCodigoAdmin, String nuevoCodigoProf) {
        codigoAccesoAdmin = nuevoCodigoAdmin;
        codigoAccesoProf = nuevoCodigoProf;
    }
}
