package DAO;

import Base.DatabaseConnection;
import modelo.Usuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {
    private static final Logger logger = LogManager.getLogger(UsuarioDAO.class);

    // Validar usuario para inicio de sesión
    public Usuario validarUsuario(String nombre, String contrasena, String tipo) throws SQLException {
        String query = "SELECT UsuarioID, Nombre, Tipo FROM Usuarios WHERE Nombre = ? AND Contraseña = ? AND Tipo = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, contrasena);
            stmt.setString(3, tipo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            String.valueOf(rs.getInt("UsuarioID")),
                            rs.getString("Nombre"),
                            rs.getString("Tipo"),
                            contrasena
                    );
                }
            }
        }
        logger.warn("Usuario no encontrado o credenciales incorrectas: Nombre={}, Tipo={}", nombre, tipo);
        return null;
    }

    // CRUD para usuarios
    public void agregarUsuario(Usuario usuario) throws SQLException {
        String query = "INSERT INTO Usuarios (Nombre, Tipo, Contraseña) VALUES (?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTipoUsuario());
            stmt.setString(3, usuario.getContrasena());
            stmt.executeUpdate();
            logger.info("Usuario agregado: {}", usuario.getNombre());
        }
    }

    public List<Usuario> listarUsuarios(String tipoUsuarioActual) throws SQLException {
        String query = tipoUsuarioActual.equals("Administrador") ?
                "SELECT UsuarioID, Nombre, Tipo FROM Usuarios" :
                "SELECT UsuarioID, Nombre, Tipo FROM Usuarios WHERE Tipo = 'Alumno'";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                        String.valueOf(rs.getInt("UsuarioID")),
                        rs.getString("Nombre"),
                        rs.getString("Tipo"),
                        ""
                ));
            }
            logger.info("Usuarios listados: {}", usuarios.size());
        }
        return usuarios;
    }

    public void actualizarUsuario(Usuario usuario) throws SQLException {
        String query = "UPDATE Usuarios SET Nombre = ?, Tipo = ?, Contraseña = ? WHERE UsuarioID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, usuario.getNombre());
            stmt.setString(2, usuario.getTipoUsuario());
            stmt.setString(3, usuario.getContrasena());
            stmt.setInt(4, Integer.parseInt(usuario.getUsuarioID()));
            stmt.executeUpdate();
            logger.info("Usuario actualizado: {}", usuario.getNombre());
        }
    }

    public void eliminarUsuario(String usuarioID) throws SQLException {
        String query = "DELETE FROM Usuarios WHERE UsuarioID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(usuarioID));
            stmt.executeUpdate();
            logger.info("Usuario eliminado con ID: {}", usuarioID);
        }
    }

    public Usuario buscarUsuarioPorID(String usuarioID) throws SQLException {
        String query = "SELECT UsuarioID, Nombre, Tipo FROM Usuarios WHERE UsuarioID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(usuarioID));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            String.valueOf(rs.getInt("UsuarioID")),
                            rs.getString("Nombre"),
                            rs.getString("Tipo"),
                            ""
                    );
                }
            }
        }
        return null;
    }
}
