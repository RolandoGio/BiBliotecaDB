package modelo;

import java.time.LocalDate;

public class Prestamo {
    private int prestamoID;
    private Usuario usuario;
    private Documento documento;
    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucion;
    private double mora;

    public Prestamo(int prestamoID, Usuario usuario, Documento documento, LocalDate fechaPrestamo, LocalDate fechaDevolucion, double mora) {
        this.prestamoID = prestamoID;
        this.usuario = usuario;
        this.documento = documento;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.mora = mora;
    }

    public Prestamo(Usuario usuario, Documento documento, LocalDate fechaPrestamo, LocalDate fechaDevolucion) {
        this(0, usuario, documento, fechaPrestamo, fechaDevolucion, 0.0);
    }

    public int getPrestamoID() {
        return prestamoID;
    }

    public void setPrestamoID(int prestamoID) {
        this.prestamoID = prestamoID;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public double getMora() {
        return mora;
    }

    public void setMora(double mora) {
        this.mora = mora;
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "prestamoID=" + prestamoID +
                ", usuario=" + usuario +
                ", documento=" + documento +
                ", fechaPrestamo=" + fechaPrestamo +
                ", fechaDevolucion=" + fechaDevolucion +
                ", mora=" + mora +
                '}';
    }
}
