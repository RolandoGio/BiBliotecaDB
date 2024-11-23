package gui;

import DAO.PrestamoDAO;
import DAO.DocumentoDAO;
import DAO.UsuarioDAO;
import modelo.Prestamo;
import modelo.Usuario;
import modelo.Documento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class LoanManagementView extends JFrame {
    private final PrestamoDAO prestamoDAO;
    private final UsuarioDAO usuarioDAO;
    private final DocumentoDAO documentoDAO;
    private final Usuario usuarioActual;
    private JTable table;
    private DefaultTableModel tableModel;

    public LoanManagementView(Usuario usuarioActual) {
        this.usuarioActual = usuarioActual;
        this.prestamoDAO = new PrestamoDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.documentoDAO = new DocumentoDAO();

        setTitle("Gestión de Préstamos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 500);
        setLayout(new BorderLayout());

        // Tabla de préstamos
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Préstamos Registrados"));
        String[] columnNames = {"ID Préstamo", "ID Usuario", "Nombre Usuario", "ID Documento", "Título Documento", "Fecha Préstamo", "Fecha Devolución", "Mora"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        cargarPrestamos();
        panelTabla.add(new JScrollPane(table), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnRegistrar = new JButton("Registrar Préstamo");
        JButton btnDevolver = new JButton("Devolver Préstamo");
        JButton btnEliminar = new JButton("Eliminar Préstamo");

        // Acciones de botones
        btnRegistrar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> devolverPrestamo());
        btnEliminar.addActionListener(e -> eliminarPrestamo());

        // Restricciones según el tipo de usuario
        if (usuarioActual.getTipoUsuario().equals("Alumno")) {
            panelBotones.add(btnRegistrar);
        } else if (usuarioActual.getTipoUsuario().equals("Profesor")) {
            panelBotones.add(btnRegistrar);
            panelBotones.add(btnDevolver);
        } else if (usuarioActual.getTipoUsuario().equals("Administrador")) {
            panelBotones.add(btnRegistrar);
            panelBotones.add(btnDevolver);
            panelBotones.add(btnEliminar);
        }

        add(panelTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    private void cargarPrestamos() {
        try {
            List<Prestamo> prestamos = prestamoDAO.listarPrestamos();
            tableModel.setRowCount(0); // Limpiar la tabla
            for (Prestamo prestamo : prestamos) {
                tableModel.addRow(new Object[]{
                        prestamo.getPrestamoID(),
                        prestamo.getUsuario().getUsuarioID(),
                        prestamo.getUsuario().getNombre(),
                        prestamo.getDocumento().getDocumentoID(),
                        prestamo.getDocumento().getTitulo(),
                        prestamo.getFechaPrestamo(),
                        prestamo.getFechaDevolucion(),
                        prestamo.getMora()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar préstamos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarPrestamo() {
        try {
            String usuarioIDStr = JOptionPane.showInputDialog(this, "Ingrese el ID del Usuario:");
            String documentoIDStr = JOptionPane.showInputDialog(this, "Ingrese el ID del Documento:");

            if (usuarioIDStr == null || documentoIDStr == null) {
                return; // Cancelar si el usuario cierra el cuadro de diálogo
            }

            int usuarioID = Integer.parseInt(usuarioIDStr);
            int documentoID = Integer.parseInt(documentoIDStr);

            // Buscar usuario y documento
            Usuario usuario = usuarioDAO.buscarUsuarioPorID(String.valueOf(usuarioID));
            Documento documento = documentoDAO.buscarDocumentoPorID(String.valueOf(documentoID));

            if (usuario == null || documento == null) {
                JOptionPane.showMessageDialog(this, "Usuario o Documento no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Crear objeto Prestamo
            Prestamo prestamo = new Prestamo(usuario, documento, LocalDate.now(), null);

            // Registrar el préstamo
            prestamoDAO.agregarPrestamo(prestamo);
            cargarPrestamos();
            JOptionPane.showMessageDialog(this, "Préstamo registrado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese valores numéricos válidos para los IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverPrestamo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int prestamoID = (int) tableModel.getValueAt(selectedRow, 0);
            String fechaDevolucionStr = JOptionPane.showInputDialog(this, "Ingrese la fecha de devolución (YYYY-MM-DD):");

            try {
                Prestamo prestamo = prestamoDAO.buscarPrestamoPorID(prestamoID);
                prestamo.setFechaDevolucion(LocalDate.parse(fechaDevolucionStr));
                prestamoDAO.actualizarPrestamo(prestamo);
                cargarPrestamos();
                JOptionPane.showMessageDialog(this, "Préstamo devuelto con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al devolver préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para devolver.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void eliminarPrestamo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int prestamoID = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este préstamo?", "Confirmar", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    prestamoDAO.eliminarPrestamo(prestamoID);
                    cargarPrestamos();
                    JOptionPane.showMessageDialog(this, "Préstamo eliminado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }
}
