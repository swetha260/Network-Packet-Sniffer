package netpacksniff.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.UIManager;

class TableRenderer extends JLabel implements TableCellRenderer {

    private static final long serialVersionUID = 1L; // Added serialVersionUID

    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

    public TableRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (isSelected) {
            super.setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            super.setForeground(table.getForeground());
            super.setBackground(table.getBackground());
        }

        setFont(table.getFont());

        if (hasFocus) {
            setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
        } else {
            setBorder(noFocusBorder);
        }

        if (value == null) {
            setText("Not Available");
            return this;
        }

        setText(value.toString());

        if (value.getClass().equals(Integer.class) || value.getClass().equals(Long.class)) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        // ---- begin optimization to avoid painting background ----
        Color back = getBackground();
        boolean colorMatch = (back != null) && (back.equals(table.getBackground())) && table.isOpaque();
        setOpaque(!colorMatch);
        // ---- end optimization to avoid painting background ----

        return this;
    }
}
