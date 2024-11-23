package gui;

import DAO.DocumentoDAO;
import modelo.Documento;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class DocumentDialog extends JDialog {
    private final DocumentoDAO documentoDAO;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JComboBox<String> cmbTipoDocumento;
    private JTextField txtCantidadDisponible;
    private JTextField txtPrecio;
    private JTextField txtUbicacionFisica; // Declaración de txtUbicacionFisica
    private JTextArea txtInfoAdicional;
    private final boolean isEditMode;
    private Documento documentoActual;

    public DocumentDialog(JFrame parent, DocumentoDAO documentoDAO, Documento documentoActual) {
        super(parent, "Gestión de Documento", true);
        this.documentoDAO = documentoDAO;
        this.documentoActual = documentoActual;
        this.isEditMode = documentoActual != null;

        setSize(400, 400);
        setLayout(new BorderLayout());

        // Título del diálogo
        JLabel lblTitulo = new JLabel(isEditMode ? "Editar Documento" : "Agregar Nuevo Documento", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel central con campos de entrada
        JPanel panelCampos = new JPanel(new GridLayout(7, 2, 10, 10)); // Ajuste a 7 filas
        panelCampos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTituloDoc = new JLabel("Título:");
        txtTitulo = new JTextField();
        JLabel lblAutor = new JLabel("Autor:");
        txtAutor = new JTextField();
        JLabel lblTipoDocumento = new JLabel("Tipo de Documento:");
        cmbTipoDocumento = new JComboBox<>(new String[]{"Libro", "Revista", "CD", "Tesis"});
        JLabel lblCantidadDisponible = new JLabel("Cantidad Disponible:");
        txtCantidadDisponible = new JTextField();
        JLabel lblPrecio = new JLabel("Precio:");
        txtPrecio = new JTextField();
        JLabel lblUbicacionFisica = new JLabel("Ubicación Física:");
        txtUbicacionFisica = new JTextField(); // Inicialización de txtUbicacionFisica
        JLabel lblInfoAdicional = new JLabel("Información Adicional:");
        txtInfoAdicional = new JTextArea(3, 20);
        txtInfoAdicional.setLineWrap(true);
        txtInfoAdicional.setWrapStyleWord(true);

        panelCampos.add(lblTituloDoc);
        panelCampos.add(txtTitulo);
        panelCampos.add(lblAutor);
        panelCampos.add(txtAutor);
        panelCampos.add(lblTipoDocumento);
        panelCampos.add(cmbTipoDocumento);
        panelCampos.add(lblCantidadDisponible);
        panelCampos.add(txtCantidadDisponible);
        panelCampos.add(lblPrecio);
        panelCampos.add(txtPrecio);
        panelCampos.add(lblUbicacionFisica);
        panelCampos.add(txtUbicacionFisica);
        panelCampos.add(lblInfoAdicional);
        panelCampos.add(new JScrollPane(txtInfoAdicional));

        add(panelCampos, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelBotones = new JPanel();
        JButton btnGuardar = new JButton(isEditMode ? "Actualizar" : "Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarDocumento());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        cargarDatos();

        setLocationRelativeTo(parent);
    }

    private void cargarDatos() {
        if (isEditMode && documentoActual != null) {
            txtTitulo.setText(documentoActual.getTitulo());
            txtAutor.setText(documentoActual.getAutor());
            cmbTipoDocumento.setSelectedItem(documentoActual.getTipoDocumento());
            txtCantidadDisponible.setText(String.valueOf(documentoActual.getCantidadDisponible()));
            txtPrecio.setText(String.valueOf(documentoActual.getPrecio()));
            txtInfoAdicional.setText(documentoActual.getInfoAdicional());
            txtUbicacionFisica.setText(documentoActual.getUbicacionFisica()); // Carga de la ubicación física
        }
    }

    private void guardarDocumento() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String tipoDocumento = Objects.requireNonNull(cmbTipoDocumento.getSelectedItem()).toString();
        String cantidadDisponibleStr = txtCantidadDisponible.getText().trim();
        String precioStr = txtPrecio.getText().trim();
        String infoAdicional = txtInfoAdicional.getText().trim();
        String ubicacionFisica = txtUbicacionFisica.getText().trim(); // Obtención de la ubicación física

        if (titulo.isEmpty() || autor.isEmpty() || cantidadDisponibleStr.isEmpty() || precioStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int cantidadDisponible = Integer.parseInt(cantidadDisponibleStr);
            double precio = Double.parseDouble(precioStr);

            if (isEditMode) {
                documentoActual.setTitulo(titulo);
                documentoActual.setAutor(autor);
                documentoActual.setTipoDocumento(tipoDocumento);
                documentoActual.setCantidadDisponible(cantidadDisponible);
                documentoActual.setPrecio(precio);
                documentoActual.setInfoAdicional(infoAdicional);
                documentoActual.setUbicacionFisica(ubicacionFisica); // Asignación de la ubicación física

                documentoDAO.actualizarDocumento(documentoActual);
                JOptionPane.showMessageDialog(this, "Documento actualizado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Documento nuevoDocumento = new Documento(null, titulo, autor, tipoDocumento, cantidadDisponible, precio, infoAdicional, ubicacionFisica);
                documentoDAO.agregarDocumento(nuevoDocumento);
                JOptionPane.showMessageDialog(this, "Documento agregado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            dispose();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad disponible y precio deben ser valores numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
