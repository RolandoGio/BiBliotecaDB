package modelo;

import DAO.PrestamoDAO;
import DAO.DocumentoDAO;
import DAO.UsuarioDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class SistemaBiblioteca {
    private final UsuarioDAO usuarioDAO;
    private final DocumentoDAO documentoDAO;
    private final PrestamoDAO prestamoDAO;

    public SistemaBiblioteca() {
        usuarioDAO = new UsuarioDAO();
        documentoDAO = new DocumentoDAO();
        prestamoDAO = new PrestamoDAO();
    }

    public void listarDocumentos() {
        try {
            documentoDAO.listarDocumentos().forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error al listar documentos: " + e.getMessage());
        }
    }

    public void registrarPrestamo(String usuarioID, String documentoID) {
        try {
            Usuario usuario = usuarioDAO.buscarUsuarioPorID(usuarioID);
            Documento documento = documentoDAO.buscarDocumentoPorID(documentoID);

            if (usuario == null || documento == null) {
                System.err.println("Usuario o documento no encontrado.");
                return;
            }

            Prestamo prestamo = new Prestamo(usuario, documento, LocalDate.now(), null);
            prestamoDAO.agregarPrestamo(prestamo);

            System.out.println("Préstamo registrado con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al registrar préstamo: " + e.getMessage());
        }
    }

    public void eliminarPrestamo(int prestamoID) {
        try {
            prestamoDAO.eliminarPrestamo(prestamoID);
            System.out.println("Préstamo eliminado con éxito.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar préstamo: " + e.getMessage());
        }
    }

    public void listarUsuarios() {
        try {
            usuarioDAO.listarUsuarios("Administrador").forEach(System.out::println);
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
    }
}
