package modelo;

public class Usuario {
    private String usuarioID; // Cambiado de int a String
    private String nombre;
    private String tipoUsuario;
    private String contrasena;

    public Usuario(String usuarioID, String nombre, String tipoUsuario, String contrasena) {
        this.usuarioID = usuarioID;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
        this.contrasena = contrasena;
    }

    public Usuario(String nombre, String tipoUsuario, String contrasena) {
        this(null, nombre, tipoUsuario, contrasena);
    }

    public String getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(String usuarioID) {
        this.usuarioID = usuarioID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "usuarioID='" + usuarioID + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipoUsuario='" + tipoUsuario + '\'' +
                '}';
    }
}
