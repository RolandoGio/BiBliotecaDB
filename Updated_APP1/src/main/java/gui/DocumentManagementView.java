package gui;

import DAO.DocumentoDAO;
import modelo.Documento;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DocumentManagementView extends JFrame {
    private final DocumentoDAO documentoDAO;
    private final Usuario usuarioActual;
    private JTable table;
    private DefaultTableModel tableModel;

    public DocumentManagementView(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.documentoDAO = new DocumentoDAO();

        setTitle("Gestión de Documentos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        // Panel para mostrar documentos
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Documentos Disponibles"));
        String[] columnNames = {"ID", "Título", "Autor", "Tipo", "Cantidad Disponible", "Precio","UbicacionFisica"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        cargarDocumentos();
        panelTabla.add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel con botones de gestión
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.addActionListener(e -> abrirDialogoDocumento(null));
        btnEditar.addActionListener(e -> editarDocumento());
        btnEliminar.addActionListener(e -> eliminarDocumento());

        // Botones según tipo de usuario
        if (usuarioActual.getTipoUsuario().equals("Administrador") || usuarioActual.getTipoUsuario().equals("Profesor")) {
            panelBotones.add(btnAgregar);
            panelBotones.add(btnEditar);
            panelBotones.add(btnEliminar);
        } else {
            panelBotones.setVisible(false);
        }

        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void cargarDocumentos() {
        try {
            List<Documento> documentos = documentoDAO.listarDocumentos();
            tableModel.setRowCount(0);
            for (Documento documento : documentos) {
                tableModel.addRow(new Object[]{
                        documento.getDocumentoID(),
                        documento.getTitulo(),
                        documento.getAutor(),
                        documento.getTipoDocumento(),
                        documento.getCantidadDisponible(),
                        documento.getPrecio(),
                        documento.getUbicacionFisica()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar documentos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirDialogoDocumento(Documento documento) {
        DocumentDialog dialog = new DocumentDialog(this, documentoDAO, documento);
        dialog.setVisible(true);
        cargarDocumentos(); // Recargar después de cerrar el diálogo
    }

    private void editarDocumento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String documentoID = (String) tableModel.getValueAt(selectedRow, 0); // Cambio a String
            try {
                Documento documento = documentoDAO.buscarDocumentoPorID(documentoID);
                abrirDialogoDocumento(documento);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cargar el documento para editar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un documento para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarDocumento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            String documentoID = (String) tableModel.getValueAt(selectedRow, 0); // Cambio a String
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este documento?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    documentoDAO.eliminarDocumento(documentoID);
                    cargarDocumentos();
                    JOptionPane.showMessageDialog(this, "Documento eliminado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar documento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un documento para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
