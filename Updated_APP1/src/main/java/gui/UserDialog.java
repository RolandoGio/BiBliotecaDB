package gui;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

public class UserDialog extends JDialog {
    private final JTextField txtNombre;
    private final JPasswordField txtContrasena; // Campo para la contraseña
    private final JComboBox<String> cmbTipoUsuario;

    private Usuario usuario;

    public UserDialog(Frame parent, String title, Usuario usuario) {
        super(parent, title, true);
        this.usuario = usuario;

        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(350, 250);

        // Campos del formulario
        add(new JLabel("Nombre:"));
        txtNombre = new JTextField(usuario != null ? usuario.getNombre() : "");
        add(txtNombre);

        add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField(usuario != null ? usuario.getContrasena() : "");
        add(txtContrasena);

        add(new JLabel("Tipo de Usuario:"));
        cmbTipoUsuario = new JComboBox<>(new String[]{"Administrador", "Profesor", "Alumno"});
        if (usuario != null) {
            cmbTipoUsuario.setSelectedItem(usuario.getTipoUsuario());
        }
        add(cmbTipoUsuario);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            this.usuario = new Usuario(
                    usuario != null ? usuario.getUsuarioID() : String.valueOf(0),
                    txtNombre.getText(),
                    (String) cmbTipoUsuario.getSelectedItem(),
                    new String(txtContrasena.getPassword()) // Obtener la contraseña como String
            );
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        add(btnGuardar);
        add(btnCancelar);

        setLocationRelativeTo(parent);
    }

    public Optional<Usuario> getUsuario() {
        return Optional.ofNullable(usuario);
    }
}
