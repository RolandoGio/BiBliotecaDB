package gui;

import DAO.UsuarioDAO;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final UsuarioDAO usuarioDAO;
    private JTextField txtNombre;
    private JPasswordField txtPassword;
    private JComboBox<String> cmbTipo;

    public LoginView() {
        usuarioDAO = new UsuarioDAO();

        setTitle("Sistema de Biblioteca - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLayout(new BorderLayout());
        setResizable(false);

        // Encabezado
        JPanel panelEncabezado = new JPanel(new FlowLayout());
        panelEncabezado.setBackground(new Color(4, 52, 84));
        JLabel lblTitulo = new JLabel("Bienvenido al Sistema de Biblioteca");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo);

        // Formulario de Login
        JPanel panelFormulario = new JPanel(new GridLayout(3, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel lblUsuario = new JLabel("Nombre de Usuario:");
        txtNombre = new JTextField();
        JLabel lblPassword = new JLabel("Contraseña:");
        txtPassword = new JPasswordField();
        JLabel lblTipo = new JLabel("Tipo de Usuario:");
        cmbTipo = new JComboBox<>(new String[]{"Alumno", "Profesor", "Administrador"});

        panelFormulario.add(lblUsuario);
        panelFormulario.add(txtNombre);
        panelFormulario.add(lblPassword);
        panelFormulario.add(txtPassword);
        panelFormulario.add(lblTipo);
        panelFormulario.add(cmbTipo);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnIngresar = new JButton("Ingresar");
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnSalir = new JButton("Salir");
        btnIngresar.setBackground(new Color(4, 52, 84));
        btnIngresar.setForeground(Color.WHITE);
        btnRegistrar.setBackground(new Color(4, 52, 84));
        btnRegistrar.setForeground(Color.WHITE);
        btnSalir.setBackground(new Color(4, 52, 84));
        btnSalir.setForeground(Color.WHITE);

        btnIngresar.addActionListener(e -> autenticarUsuario());
        btnRegistrar.addActionListener(e -> abrirPantallaRegistro());
        btnSalir.addActionListener(e -> cerrarAplicacion());

        panelBotones.add(btnIngresar);
        panelBotones.add(btnRegistrar);
        panelBotones.add(btnSalir);

        // Añadir componentes al JFrame
        add(panelEncabezado, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void autenticarUsuario() {
        String nombre = txtNombre.getText().trim();
        String contrasena = new String(txtPassword.getPassword());
        String tipo = (String) cmbTipo.getSelectedItem();

        if (nombre.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Usuario usuario = usuarioDAO.validarUsuario(nombre, contrasena, tipo);
            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Bienvenido, " + nombre + " (" + tipo + ")!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                SwingUtilities.invokeLater(() -> new MainMenu(usuario).setVisible(true));
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Inténtelo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al autenticar usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirPantallaRegistro() {
        SwingUtilities.invokeLater(() -> new RegisterView().setVisible(true));
        dispose();
    }

    private void cerrarAplicacion() {
        int confirm = JOptionPane.showConfirmDialog(
                this, "¿Está seguro de que desea salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}

