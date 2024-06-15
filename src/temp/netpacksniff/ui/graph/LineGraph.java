package netpacksniff.ui.graph;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;
import java.util.Vector;
import java.io.Serializable;

public class LineGraph extends JPanel {
    private static final long serialVersionUID = 1L; // Added serialVersionUID for LineGraph class

    private String[] labels;
    private Vector<long[]> values = new Vector<>();
    private long maxValue = Long.MIN_VALUE;
    private long minValue = Long.MAX_VALUE;
    private boolean autoMax;
    private boolean autoMin;
    private int marginY = 20;
    private int marginX = 20;

    private Color[] colors = {
        Color.blue, Color.green, Color.yellow.darker(), Color.red, Color.cyan, Color.pink, Color.orange
    };

    public LineGraph(String[] labels) {
        this(labels, null, Long.MAX_VALUE, Long.MIN_VALUE, true, true);
    }

    public LineGraph(String[] labels, long[][] values) {
        this(labels, values, Long.MAX_VALUE, Long.MIN_VALUE, true, true);
    }

    public LineGraph(String[] labels, long[][] values, long minValue, long maxValue) {
        this(labels, values, minValue, maxValue, false, false);
    }

    public LineGraph(String[] labels, long[][] values, long minValue, long maxValue, boolean autoMin, boolean autoMax) {
        this.labels = labels;
        this.autoMax = autoMax;
        this.autoMin = autoMin;
        this.minValue = minValue;
        this.maxValue = maxValue;

        if (values != null) {
            for (long[] value : values) {
                this.values.addElement(value);
                if (autoMin || autoMax) {
                    for (long v : value) {
                        if (autoMax && v > maxValue) maxValue = v;
                        if (autoMin && v < minValue) minValue = v;
                    }
                }
            }
        }

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(new GraphPane());
        add(new LabelPane());
    }

    public void addValue(long[] values) {
        this.values.addElement(values);

        if (autoMin || autoMax) {
            for (long value : values) {
                if (autoMax && value > maxValue) maxValue = value;
                if (autoMin && value < minValue) minValue = value;
            }
        }
        repaint();
    }

    public void clear() {
        values.removeAllElements();
        maxValue = Long.MIN_VALUE;
        minValue = Long.MAX_VALUE;
        repaint();
    }

    public void setMinValue(long minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(long maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValueAutoSet(boolean autoMin) {
        this.autoMin = autoMin;
    }

    public void setMaxValueAutoSet(boolean autoMax) {
        this.autoMax = autoMax;
    }

    private class GraphPane extends JPanel implements Serializable {
        private static final long serialVersionUID = 2L; // Added serialVersionUID for GraphPane class

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.white);

            if (labels == null || values == null || values.isEmpty()) return;

            int ylabelWidth = 0;
            for (int i = 0; i < 4; i++) {
                int width = g.getFontMetrics().stringWidth(String.valueOf((double)(maxValue - (maxValue - minValue) * i / 4.0)));
                if (width > ylabelWidth) ylabelWidth = width;
            }

            long h = getHeight() - marginY * 2;
            long w = getWidth();
            long range = maxValue - minValue;
            double d = (double)(w - marginX * 2) / (values.size() - 1);
            double x = d + marginX + ylabelWidth;

            g.setColor(Color.black);
            g.drawLine(marginX + ylabelWidth, 0, marginX + ylabelWidth, getHeight());

            g.setColor(Color.gray);
            for (int i = 0; i < 5; i++) {
                int y = marginY + (getHeight() - marginY * 2) / 4 * i;
                g.drawLine(marginX + ylabelWidth, y, getWidth(), y);
                g.drawString(String.valueOf((double)(maxValue - (maxValue - minValue) * i / 4.0)), marginX - 5, y);
            }

            long[] previousValues = values.firstElement();
            for (int i = 1; i < values.size(); i++, x += d) {
                long[] currentValues = values.elementAt(i);

                for (int j = 0; j < currentValues.length; j++) {
                    Color color = colors[j % colors.length];
                    for (int k = 0; k < j / colors.length; k++) color = color.darker();
                    g.setColor(color);

                    g.drawLine((int)(x - d), (int)(h + marginY - (previousValues[j] - minValue) * h / range),
                               (int)x, (int)(h + marginY - (currentValues[j] - minValue) * h / range));
                }

                previousValues = currentValues;
            }
        }
    }

    private class LabelPane extends JPanel implements Serializable {
        private static final long serialVersionUID = 3L; // Added serialVersionUID for LabelPane class

        LabelPane() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.white);

            for (int i = 0; i < labels.length; i++) {
                JPanel container = new JPanel();
                container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
                container.setBackground(Color.white);
                JLabel label = new JLabel(labels[i], SwingConstants.LEFT);
                label.setForeground(Color.black);
                JLabel box = new JLabel("    ");
                box.setOpaque(true);

                Color color = colors[i % colors.length];
                for (int j = 0; j < i / colors.length; j++) color = color.darker();
                box.setBackground(color);

                container.add(box);
                container.add(Box.createRigidArea(new Dimension(5, 0)));
                container.add(label);
                container.setAlignmentX(0.0f);
                add(container);
                add(Box.createRigidArea(new Dimension(0, 5)));
            }

            setBorder(new CompoundBorder(BorderFactory.createLineBorder(Color.black, 1), new EmptyBorder(10, 10, 10, 10)));
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(50, 1);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(300, 200);
    }

    public static void main(String[] args) {
        String[] labels = {"Layout", "Box"};
        long[][] data = {{1, 1}, {2, 4}, {3, 2}};

        JFrame frame = new JFrame();
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        LineGraph lineGraph = new LineGraph(labels, data, 0, 10);

        frame.getContentPane().add(lineGraph);
        frame.pack();
        frame.setVisible(true);
    }
}
