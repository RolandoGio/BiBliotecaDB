package DAO;

import Base.DatabaseConnection;
import modelo.Documento;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DocumentoDAO {
    private static final Logger logger = LogManager.getLogger(DocumentoDAO.class);

    // Crear (Agregar) un nuevo documento
    public void agregarDocumento(Documento documento) throws SQLException {
        String query = "INSERT INTO Documentos (Titulo, Autor, TipoDocumento, CantidadDisponible, Precio, InfoAdicional, UbicacionFisica) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, documento.getTitulo());
            stmt.setString(2, documento.getAutor());
            stmt.setString(3, documento.getTipoDocumento());
            stmt.setInt(4, documento.getCantidadDisponible());
            stmt.setDouble(5, documento.getPrecio());
            stmt.setString(6, documento.getInfoAdicional());
            stmt.setString(7, documento.getUbicacionFisica());

            stmt.executeUpdate();
            logger.info("Documento agregado: {}", documento.getTitulo());
        } catch (SQLException e) {
            logger.error("Error al agregar documento: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Listar todos los documentos
    public List<Documento> listarDocumentos() throws SQLException {
        String query = "SELECT DocumentoID, Titulo, Autor, TipoDocumento, CantidadDisponible, Precio, InfoAdicional, UbicacionFisica FROM Documentos";
        List<Documento> documentos = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                documentos.add(new Documento(
                        String.valueOf(rs.getInt("DocumentoID")),
                        rs.getString("Titulo"),
                        rs.getString("Autor"),
                        rs.getString("TipoDocumento"),
                        rs.getInt("CantidadDisponible"),
                        rs.getDouble("Precio"),
                        rs.getString("InfoAdicional"),
                        rs.getString("UbicacionFisica")
                ));
            }
            logger.info("Documentos listados: {}", documentos.size());
        } catch (SQLException e) {
            logger.error("Error al listar documentos: {}", e.getMessage(), e);
            throw e;
        }
        return documentos;
    }

    // Actualizar un documento existente
    public void actualizarDocumento(Documento documento) throws SQLException {
        String query = "UPDATE Documentos SET Titulo = ?, Autor = ?, TipoDocumento = ?, CantidadDisponible = ?, Precio = ?, InfoAdicional = ?, UbicacionFisica = ? WHERE DocumentoID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, documento.getTitulo());
            stmt.setString(2, documento.getAutor());
            stmt.setString(3, documento.getTipoDocumento());
            stmt.setInt(4, documento.getCantidadDisponible());
            stmt.setDouble(5, documento.getPrecio());
            stmt.setString(6, documento.getInfoAdicional());
            stmt.setString(7, documento.getUbicacionFisica());
            stmt.setInt(8, Integer.parseInt(documento.getDocumentoID()));
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Documento actualizado: {}", documento.getTitulo());
            } else {
                logger.warn("No se encontró un documento con ID: {}", documento.getDocumentoID());
            }
        } catch (SQLException e) {
            logger.error("Error al actualizar documento: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Eliminar un documento por ID
    public void eliminarDocumento(String documentoID) throws SQLException {
        String query = "DELETE FROM Documentos WHERE DocumentoID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(documentoID));
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Documento eliminado con ID: {}", documentoID);
            } else {
                logger.warn("No se encontró un documento con ID: {}", documentoID);
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar documento: {}", e.getMessage(), e);
            throw e;
        }
    }

    // Buscar un documento por ID
    public Documento buscarDocumentoPorID(String documentoID) throws SQLException {
        String query = "SELECT DocumentoID, Titulo, Autor, TipoDocumento, CantidadDisponible, Precio, InfoAdicional, UbicacionFisica FROM Documentos WHERE DocumentoID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(documentoID));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Documento(
                            String.valueOf(rs.getInt("DocumentoID")),
                            rs.getString("Titulo"),
                            rs.getString("Autor"),
                            rs.getString("TipoDocumento"),
                            rs.getInt("CantidadDisponible"),
                            rs.getDouble("Precio"),
                            rs.getString("InfoAdicional"),
                            rs.getString("UbicacionFisica")
                    );
                }
            }
        } catch (SQLException e) {
            logger.error("Error al buscar documento por ID: {}", e.getMessage(), e);
            throw e;
        }
        logger.warn("No se encontró un documento con ID: {}", documentoID);
        return null;
    }
}
