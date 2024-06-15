package netpacksniff.ui;

import jpcap.packet.Packet;
import netpacksniff.stat.StatisticsTaker;
import netpacksniff.ui.graph.PieGraph;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.Serializable;
import java.util.Vector;

public class CumulativeStatFrame extends StatFrame implements ListSelectionListener {

    private static final long serialVersionUID = 1L; // Add this serialVersionUID

    private final JTable table;
    private final TableModel model;
    private PieGraph pieGraph;

    private final StatisticsTaker staker;
    private int statType = 0;

    // Static method to create and display the frame
    public static CumulativeStatFrame openWindow(Vector<Packet> packets, StatisticsTaker staker) {
        CumulativeStatFrame frame = new CumulativeStatFrame(packets, staker);
        frame.setVisible(true);
        return frame;
    }

    // Constructor
    CumulativeStatFrame(Vector<Packet> packets, StatisticsTaker staker) {
        super(staker.getName());
        this.staker = staker;
        staker.analyze(packets);

        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        model = new TableModel();
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JTableHeader header = table.getTableHeader();
        Dimension dim = header.getPreferredSize();
        dim.height = 20;
        header.setPreferredSize(dim);
        JScrollPane tablePane = new JScrollPane(table);
        dim = table.getMinimumSize();
        dim.height += 25;
        tablePane.setPreferredSize(dim);

        if (staker.getLabels().length > 1) {
            pieGraph = new PieGraph(staker.getLabels(), staker.getValues(0));
            JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            splitPane.setTopComponent(tablePane);
            splitPane.setBottomComponent(pieGraph);

            getContentPane().add(splitPane);

            table.getSelectionModel().addListSelectionListener(this);
        } else {
            getContentPane().add(tablePane);
        }

        setSize(300, 300);
    }

    // Updates the table and pie chart with the latest statistics
    void fireUpdate() {
        int sel = table.getSelectedRow();
        if (pieGraph != null) {
            pieGraph.changeValue(staker.getValues(statType));
        }
        if (model != null) {
            model.update();
        }
        if (sel >= 0) {
            table.setRowSelectionInterval(sel, sel);
        }
        repaint();
    }

    // Adds a new packet to the statistics taker
    public void addPacket(Packet p) {
        staker.addPacket(p);
    }

    // Clears the current statistics
    public void clear() {
        staker.clear();
        if (pieGraph != null) {
            pieGraph.changeValue(staker.getValues(statType));
        }
        if (model != null) {
            model.update();
        }
    }

    // Listens for selection changes in the table
    public void valueChanged(ListSelectionEvent evt) {
        if (evt.getValueIsAdjusting()) {
            return;
        }

        ListSelectionModel lsm = (ListSelectionModel) evt.getSource();
        if (lsm.isSelectionEmpty()) {
            statType = 0;
        } else {
            statType = lsm.getMinSelectionIndex();
        }
        if (pieGraph != null) {
            pieGraph.changeValue(staker.getValues(statType));
        }
    }

    // Inner class for the table model
    class TableModel extends AbstractTableModel implements Serializable {
    	private static final long serialVersionUID = 1L;
        private String[] labels;
        private Object[][] values;

        TableModel() {
            labels = new String[staker.getLabels().length + 1];
            labels[0] = "";
            System.arraycopy(staker.getLabels(), 0, labels, 1, staker.getLabels().length);

            String[] types = staker.getStatTypes();
            values = new Object[types.length][staker.getLabels().length + 1];
            for (int i = 0; i < values.length; i++) {
                values[i][0] = types[i];
                long[] v = staker.getValues(i);
                for (int j = 0; j < v.length; j++) {
                    values[i][j + 1] = v[j];
                }
            }
        }

        @Override
        public String getColumnName(int c) {
            return labels[c];
        }

        @Override
        public int getColumnCount() {
            return labels.length;
        }

        @Override
        public int getRowCount() {
            return values.length;
        }

        @Override
        public Object getValueAt(int row, int column) {
            return values[row][column];
        }

        void update() {
            String[] types = staker.getStatTypes();
            values = new Object[types.length][staker.getLabels().length + 1];
            for (int i = 0; i < values.length; i++) {
                values[i][0] = types[i];
                long[] v = staker.getValues(i);
                for (int j = 0; j < v.length; j++) {
                    values[i][j + 1] = v[j];
                }
            }
            fireTableDataChanged();
        }
    }
}
