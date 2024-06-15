package netpacksniff.ui;

import javax.swing.*;
import javax.swing.table.*;

import netpacksniff.Captor;
import netpacksniff.analyzer.PacketAnalyzerAbstract;

import java.util.*;
import java.awt.*;
import jpcap.packet.*;

public class Table extends JComponent {

    private static final long serialVersionUID = 1L;
    TableModel model;
    TableSorter sorter;
    Vector<TableView> views = new Vector<>();
    Captor captor;

    public Table(TablePane parent, Captor captor) {
        this.captor = captor;
        model = new TableModel();
        sorter = new TableSorter(model);
        JTable table = new JTable(sorter);
        sorter.addMouseListenerToHeaderInTable(table); // ADDED THIS
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(parent);
        table.setDefaultRenderer(Object.class, new TableRenderer());
        JScrollPane tableView = new JScrollPane(table);

        setLayout(new BorderLayout());
        add(tableView, BorderLayout.CENTER);
    }

    void fireTableChanged() {
        model.fireTableRowsInserted(captor.getPackets().size() - 1, captor.getPackets().size() - 1);
    }

    void clear() {
        model.fireTableStructureChanged();
        model.fireTableDataChanged();
    }

    void setTableView(PacketAnalyzerAbstract analyzer, String name, boolean set) {
        if (set) {
            views.addElement(new TableView(analyzer, name));
        } else {
            views.removeIf(view -> view.analyzer == analyzer && view.valueName.equals(name));
        }
        model.fireTableStructureChanged();
    }

    String[] getTableViewStatus() {
        String[] status = new String[views.size()];

        for (int i = 0; i < status.length; i++) {
            TableView view = views.elementAt(i);
            status[i] = view.analyzer.getProtocolName() + ":" + view.valueName;
        }

        return status;
    }

    class TableView {
        PacketAnalyzerAbstract analyzer;
        String valueName;

        TableView(PacketAnalyzerAbstract analyzer, String name) {
            this.analyzer = analyzer;
            this.valueName = name;
        }
    }

    class TableModel extends AbstractTableModel {

        private static final long serialVersionUID = 1L;

        public int getRowCount() {
            return captor.getPackets().size();
        }

        public int getColumnCount() {
            return views.size() + 1; // Adding 1 for the "No." column
        }

        public Object getValueAt(int row, int column) {
            if (captor.getPackets().size() <= row) return "";
            Packet packet = captor.getPackets().get(row);

            if (column == 0) {
                return row + 1; // Displaying row numbers starting from 1
            }

            TableView view = views.elementAt(column - 1);

            if (view.analyzer.isAnalyzable(packet)) {
                synchronized (view.analyzer) {
                    view.analyzer.analyze(packet);
                    Object obj = view.analyzer.getValue(view.valueName);

                    if (obj instanceof Vector) {
                        Vector<?> vector = (Vector<?>) obj;
                        if (!vector.isEmpty()) {
                            return vector.elementAt(0);
                        } else {
                            return null;
                        }
                    } else {
                        return obj;
                    }
                }
            } else {
                return null;
            }
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public String getColumnName(int column) {
            if (column == 0) {
                return "No."; // Column header for row numbers
            }
            return views.elementAt(column - 1).valueName; // Column header for packet analyzer values
        }
    }
}
