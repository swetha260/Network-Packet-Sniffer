package netpacksniff.ui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class TableMap extends AbstractTableModel implements TableModelListener {
    private static final long serialVersionUID = 1L; // Added serialVersionUID

    protected TableModel model;

    public TableModel getModel() {
        return model;
    }

    public void setModel(TableModel model) {
        this.model = model;
        if (model instanceof AbstractTableModel) {
            ((AbstractTableModel) model).addTableModelListener(this); // Cast and add listener
        }
    }

    // By default, implement TableModel by forwarding all messages
    // to the model.

    public Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(aRow, aColumn);
    }

    public void setValueAt(Object aValue, int aRow, int aColumn) {
        model.setValueAt(aValue, aRow, aColumn);
    }

    public int getRowCount() {
        return (model == null) ? 0 : model.getRowCount();
    }

    public int getColumnCount() {
        return (model == null) ? 0 : model.getColumnCount();
    }

    public String getColumnName(int aColumn) {
        return model.getColumnName(aColumn);
    }

    public Class<?> getColumnClass(int aColumn) {
        return model.getColumnClass(aColumn); // Changed to Class<?>
    }

    public boolean isCellEditable(int row, int column) {
        return model.isCellEditable(row, column);
    }

    // Implementation of the TableModelListener interface,

    // By default forward all events to all the listeners.
    public void tableChanged(TableModelEvent e) {
        fireTableChanged(e);
    }
}
