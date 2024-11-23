package gui;

import modelo.Documento;
import modelo.Prestamo;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class LoanDialog extends JDialog {
    private final JComboBox<Usuario> cmbUsuario;
    private final JComboBox<Documento> cmbDocumento;
    private final JTextField txtFechaPrestamo;
    private Prestamo prestamo;

    public LoanDialog(Frame parent, String title, List<Usuario> usuarios, List<Documento> documentos, Prestamo prestamo) {
        super(parent, title, true);
        this.prestamo = prestamo;

        setLayout(new GridLayout(4, 2, 10, 10));
        setSize(400, 250);

        // Campos del formulario
        add(new JLabel("Usuario:"));
        cmbUsuario = new JComboBox<>(usuarios.toArray(new Usuario[0]));
        if (prestamo != null) {
            cmbUsuario.setSelectedItem(prestamo.getUsuario());
        }
        add(cmbUsuario);

        add(new JLabel("Documento:"));
        cmbDocumento = new JComboBox<>(documentos.toArray(new Documento[0]));
        if (prestamo != null) {
            cmbDocumento.setSelectedItem(prestamo.getDocumento());
        }
        add(cmbDocumento);

        add(new JLabel("Fecha de Préstamo:"));
        txtFechaPrestamo = new JTextField(prestamo != null ? prestamo.getFechaPrestamo().toString() : LocalDate.now().toString());
        add(txtFechaPrestamo);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> {
            Usuario usuarioSeleccionado = (Usuario) cmbUsuario.getSelectedItem();
            Documento documentoSeleccionado = (Documento) cmbDocumento.getSelectedItem();

            this.prestamo = new Prestamo(
                    prestamo != null ? prestamo.getPrestamoID() : 0,
                    usuarioSeleccionado,
                    documentoSeleccionado,
                    LocalDate.parse(txtFechaPrestamo.getText()),
                    null,
                    0.0
            );
            dispose();
        });

        btnCancelar.addActionListener(e -> dispose());

        add(btnGuardar);
        add(btnCancelar);

        setLocationRelativeTo(parent);
    }

    public Optional<Prestamo> getPrestamo() {
        return Optional.ofNullable(prestamo);
    }
}

