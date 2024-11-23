package modelo;

public class Documento {
    private String documentoID; // Aceptará null para nuevos documentos
    private String titulo;
    private String autor;
    private String tipoDocumento;
    private int cantidadDisponible;
    private double precio;
    private String UbicacionFisica;
    private String infoAdicional;

    // Constructor completo
    public Documento(String documentoID, String titulo, String autor, String tipoDocumento, int cantidadDisponible, double precio, String infoAdicional, String UbicacionFisica) {
        this.documentoID = documentoID;
        this.titulo = titulo;
        this.autor = autor;
        this.tipoDocumento = tipoDocumento;
        this.cantidadDisponible = cantidadDisponible;
        this.precio = precio;
        this.UbicacionFisica = UbicacionFisica;
        this.infoAdicional = infoAdicional;
    }

    // Constructor para nuevos documentos (sin ID)
    public Documento(String titulo, String autor, String tipoDocumento, int cantidadDisponible, double precio, String infoAdicional, String UbicacionFisica) {
        this(null, titulo, autor, tipoDocumento, cantidadDisponible, precio, infoAdicional, UbicacionFisica);
    }

    // Getters y setters
    public String getDocumentoID() {
        return documentoID;
    }

    public void setDocumentoID(String documentoID) {
        this.documentoID = documentoID;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getInfoAdicional() {
        return infoAdicional;
    }

    public void setInfoAdicional(String infoAdicional) {
        this.infoAdicional = infoAdicional;
    }

    public String getUbicacionFisica() {
        return UbicacionFisica; // CORREGIDO
    }

    public void setUbicacionFisica(String UbicacionFisica) {
        this.UbicacionFisica = UbicacionFisica; // CORREGIDO
    }

    @Override
    public String toString() {
        return "Documento{" +
                "documentoID='" + documentoID + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", cantidadDisponible=" + cantidadDisponible +
                ", precio=" + precio +
                ", infoAdicional='" + infoAdicional + '\'' +
                ", UbicacionFisica='" + UbicacionFisica + '\'' +
                '}';
    }
}
