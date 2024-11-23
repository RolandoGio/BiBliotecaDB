package gui;

import modelo.Documento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class DocumentTableModel extends AbstractTableModel {
    private final List<Documento> documentos;
    private final String[] columnNames;

    public DocumentTableModel(List<Documento> documentos, String[] columnNames) {
        if (documentos == null || columnNames == null) {
            throw new IllegalArgumentException("Los parámetros 'documentos' y 'columnNames' no pueden ser null.");
        }
        this.documentos = documentos;
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return documentos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= documentos.size() || columnIndex < 0 || columnIndex >= columnNames.length) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites en getValueAt: fila " + rowIndex + ", columna " + columnIndex);
        }
        Documento documento = documentos.get(rowIndex);
        if (documento == null) {
            return "Dato no disponible"; // Si el objeto completo es null
        }

        switch (columnIndex) {
            case 0:
                return documento.getDocumentoID();
            case 1:
                return documento.getTitulo() != null ? documento.getTitulo() : "Sin título";
            case 2:
                return documento.getAutor() != null ? documento.getAutor() : "Desconocido";
            case 3:
                return documento.getTipoDocumento() != null ? documento.getTipoDocumento() : "No especificado";
            case 4:
                return documento.getCantidadDisponible();
            case 5:
                return documento.getPrecio();
            case 6:
                return documento.getUbicacionFisica();
                case 7:
                return documento.getInfoAdicional() != null ? documento.getInfoAdicional() : "No disponible";
            default:
                return null;
        }
    }


    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public Documento getDocumentoAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= documentos.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites en getDocumentoAt: fila " + rowIndex);
        }
        return documentos.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= documentos.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites en removeRow: fila " + rowIndex);
        }
        documentos.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }
}
