package gui;

import modelo.Prestamo;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class LoanTableModel extends AbstractTableModel {
    private final List<Prestamo> prestamos;
    private final String[] columnNames;

    public LoanTableModel(List<Prestamo> prestamos, String[] columnNames) {
        this.prestamos = prestamos;
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return prestamos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Prestamo prestamo = prestamos.get(rowIndex);
        switch (columnIndex) {
            case 0: return prestamo.getPrestamoID();
            case 1: return prestamo.getUsuario().getNombre();
            case 2: return prestamo.getDocumento().getTitulo();
            case 3: return prestamo.getFechaPrestamo();
            case 4: return prestamo.getFechaDevolucion();
            case 5: return prestamo.getMora();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void addRow(Prestamo prestamo) {
        prestamos.add(prestamo);
        fireTableRowsInserted(prestamos.size() - 1, prestamos.size() - 1);
    }

    public void updateRow(int rowIndex, Prestamo prestamo) {
        if (rowIndex >= 0 && rowIndex < prestamos.size()) {
            prestamos.set(rowIndex, prestamo);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < prestamos.size()) {
            prestamos.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public Prestamo getPrestamoAt(int rowIndex) {
        return prestamos.get(rowIndex);
    }
}


