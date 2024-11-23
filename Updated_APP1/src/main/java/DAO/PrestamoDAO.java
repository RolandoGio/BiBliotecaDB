package DAO;

import Base.DatabaseConnection;
import modelo.Prestamo;
import modelo.Usuario;
import modelo.Documento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static final Logger logger = LogManager.getLogger(PrestamoDAO.class);

    // Crear (Registrar) un nuevo préstamo
    public void agregarPrestamo(Prestamo prestamo) throws SQLException {
        String query = "INSERT INTO Prestamos (UsuarioID, DocumentoID, FechaPrestamo) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(prestamo.getUsuario().getUsuarioID())); // Convertimos String a int
            stmt.setInt(2, Integer.parseInt(prestamo.getDocumento().getDocumentoID())); // Convertimos String a int
            stmt.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            stmt.executeUpdate();
            logger.info("Préstamo registrado: UsuarioID={}, DocumentoID={}",
                    prestamo.getUsuario().getUsuarioID(), prestamo.getDocumento().getDocumentoID());
        } catch (SQLException e) {
            logger.error("Error al registrar préstamo: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Listar todos los préstamos
    public List<Prestamo> listarPrestamos() throws SQLException {
        String query = "SELECT p.PrestamoID, p.FechaPrestamo, p.FechaDevolucion, p.Mora, "
                + "u.UsuarioID, u.Nombre AS NombreUsuario, u.Tipo AS TipoUsuario, "
                + "d.DocumentoID, d.Titulo AS TituloDocumento, d.Autor AS AutorDocumento, "
                + "d.TipoDocumento "
                + "FROM Prestamos p "
                + "JOIN Usuarios u ON p.UsuarioID = u.UsuarioID "
                + "JOIN Documentos d ON p.DocumentoID = d.DocumentoID";

        List<Prestamo> prestamos = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        String.valueOf(rs.getInt("UsuarioID")), // Convertimos int a String
                        rs.getString("NombreUsuario"),
                        rs.getString("TipoUsuario"),
                        ""
                );
                Documento documento = new Documento(
                        String.valueOf(rs.getInt("DocumentoID")), // ID como String
                        rs.getString("TituloDocumento"),
                        rs.getString("AutorDocumento"),
                        rs.getString("TipoDocumento"),
                        0, // Cantidad disponible por defecto
                        0.0, // Precio por defecto
                        null, // Información adicional
                        null // Ubicación física
                );
                Prestamo prestamo = new Prestamo(
                        rs.getInt("PrestamoID"),
                        usuario,
                        documento,
                        rs.getDate("FechaPrestamo").toLocalDate(),
                        rs.getDate("FechaDevolucion") != null ? rs.getDate("FechaDevolucion").toLocalDate() : null,
                        rs.getDouble("Mora")
                );
                prestamos.add(prestamo);
            }
            logger.info("Préstamos listados: {}", prestamos.size());
        } catch (SQLException e) {
            logger.error("Error al listar préstamos: {}", e.getMessage(), e);
            throw e;
        }
        return prestamos;
    }

    // Actualizar un préstamo existente
    public void actualizarPrestamo(Prestamo prestamo) throws SQLException {
        String query = "UPDATE Prestamos SET FechaDevolucion = ?, Mora = ? WHERE PrestamoID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setDate(1, prestamo.getFechaDevolucion() != null ? Date.valueOf(prestamo.getFechaDevolucion()) : null);
            stmt.setDouble(2, prestamo.getMora());
            stmt.setInt(3, prestamo.getPrestamoID());
            stmt.executeUpdate();
            logger.info("Préstamo actualizado: PrestamoID={}", prestamo.getPrestamoID());
        } catch (SQLException e) {
            logger.error("Error al actualizar préstamo: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Eliminar un préstamo por ID
    public void eliminarPrestamo(int prestamoID) throws SQLException {
        String query = "DELETE FROM Prestamos WHERE PrestamoID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, prestamoID);
            stmt.executeUpdate();
            logger.info("Préstamo eliminado con ID: {}", prestamoID);
        } catch (SQLException e) {
            logger.error("Error al eliminar préstamo: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Buscar un préstamo por ID
    public Prestamo buscarPrestamoPorID(int prestamoID) throws SQLException {
        String query = "SELECT p.PrestamoID, p.FechaPrestamo, p.FechaDevolucion, p.Mora, "
                + "u.UsuarioID, u.Nombre AS NombreUsuario, u.Tipo AS TipoUsuario, "
                + "d.DocumentoID, d.Titulo AS TituloDocumento, d.Autor AS AutorDocumento, "
                + "d.TipoDocumento "
                + "FROM Prestamos p "
                + "JOIN Usuarios u ON p.UsuarioID = u.UsuarioID "
                + "JOIN Documentos d ON p.DocumentoID = d.DocumentoID "
                + "WHERE p.PrestamoID = ?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, prestamoID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario(
                            String.valueOf(rs.getInt("UsuarioID")), // Convertimos int a String
                            rs.getString("NombreUsuario"),
                            rs.getString("TipoUsuario"),
                            ""
                    );
                    Documento documento = new Documento(
                            String.valueOf(rs.getInt("DocumentoID")), // ID como String
                            rs.getString("TituloDocumento"),
                            rs.getString("AutorDocumento"),
                            rs.getString("TipoDocumento"),
                            0, // Cantidad disponible por defecto
                            0.0, // Precio por defecto
                            null, // Información adicional
                            null // Ubicación física
                    );
                    return new Prestamo(
                            rs.getInt("PrestamoID"),
                            usuario,
                            documento,
                            rs.getDate("FechaPrestamo").toLocalDate(),
                            rs.getDate("FechaDevolucion") != null ? rs.getDate("FechaDevolucion").toLocalDate() : null,
                            rs.getDouble("Mora")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar préstamo por ID: {}", e.getMessage(), e);
            throw e;
        }
        logger.warn("No se encontró un préstamo con ID: {}", prestamoID);
        return null;
    }
}
