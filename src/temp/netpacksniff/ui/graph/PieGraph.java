package netpacksniff.ui.graph;

import java.awt.*;
import javax.swing.*;
import java.io.Serializable;

public class PieGraph extends JPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String[] labels;
    private long[] values;
    
    private Color[] colors = {
        Color.blue, Color.green, Color.yellow, Color.red, Color.cyan, Color.pink, Color.orange
    };

    public PieGraph(String[] labels, long[] values) {
        this.labels = labels;
        this.values = values;
    }

    public void changeValue(long[] values) {
        this.values = values;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (labels == null || values == null || labels.length == 0 || values.length == 0) {
            return;
        }

        int r = Math.min(getWidth(), getHeight()) / 2 - 20; // Radius of the pie chart
        int x = getWidth() / 2; // Center x-coordinate of the pie chart
        int y = getHeight() / 2; // Center y-coordinate of the pie chart

        // Calculate the total sum of all values
        long sum = 0;
        for (long value : values) {
            sum += value;
        }

        // Initialize the starting angle of the pie slices
        double startAngle = 90.0;

        // Draw each slice of the pie chart
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0) {
                continue; // Skip if the value is zero
            }
            
            // Calculate the angle for this slice
            double angle = (double) values[i] * 360.0 / (double) sum;

            // Determine the color for this slice
            Color sliceColor = colors[i % colors.length];

            // Darken the color slightly for each subsequent slice
            for (int j = 0; j < i / colors.length; j++) {
                sliceColor = sliceColor.darker();
            }

            g.setColor(sliceColor);

            // Draw the pie slice
            g.fillArc(x - r, y - r, 2 * r, 2 * r, (int) startAngle, (int) -angle);

            // Update the starting angle for the next slice
            startAngle -= angle;
        }

        // Draw labels outside the pie chart
        startAngle = 90.0;
        for (int i = 0; i < values.length; i++) {
            if (values[i] == 0) {
                continue; // Skip if the value is zero
            }
            
            // Calculate the angle for this slice
            double angle = values[i] * 360.0 / sum;

            // Calculate the position to draw the label
            int labelX = (int) (Math.cos(2 * Math.PI * (startAngle - angle / 2) / 360) * (double) (r + 10));
            int labelY = (int) (Math.sin(2 * Math.PI * (startAngle - angle / 2) / 360) * (double) (r + 10));

            // Draw the label
            g.setColor(Color.black);
            g.drawString(labels[i], x + labelX, y - labelY);

            // Update the starting angle for the next slice
            startAngle -= angle;
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(100, 100); // Set preferred size of the panel
    }
}
