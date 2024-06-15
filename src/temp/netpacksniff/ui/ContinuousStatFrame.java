package netpacksniff.ui;

import java.awt.BorderLayout;
import java.util.Enumeration;
import java.util.Vector;

import jpcap.packet.Packet;
import netpacksniff.stat.StatisticsTaker;
import netpacksniff.ui.graph.LineGraph;

public class ContinuousStatFrame extends StatFrame {
    private static final long serialVersionUID = 1L;

    private final LineGraph lineGraph;     // Line graph component to display statistics
    private final StatisticsTaker staker;  // Object to calculate statistics
    private int statType;                  // Type of statistics to display
    private boolean drawTimescale;         // Flag to indicate whether to draw based on time or packet count
    private int count;                     // Number of time units or packets per graph update
    private int currentCount = 0;          // Current count of packets processed within the current time unit
    private long currentSec = 0;           // Current time unit in seconds (for time-based graphs)

    // Static method to create and display the frame
    public static ContinuousStatFrame openWindow(Vector<Packet> packets, StatisticsTaker staker) {
        ContinuousStatFrame frame = new ContinuousStatFrame(packets, 5, true, staker, 0);
        frame.setVisible(true);
        return frame;
    }

    // Constructor
    public ContinuousStatFrame(Vector<Packet> packets, int count, boolean isTime, StatisticsTaker staker, int type) {
        super(staker.getName() + " [" + staker.getStatTypes()[type] + "]");
        this.staker = staker;
        this.drawTimescale = isTime;
        this.count = count;
        this.statType = type;

        // Initialize the line graph with statistics labels
        lineGraph = new LineGraph(staker.getLabels());

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(lineGraph, BorderLayout.CENTER);
        setSize(300, 300);

        if (packets == null || packets.isEmpty()) {
            return;
        }

        initializeGraph(packets); // Initialize graph with initial packets data
    }

    // Initializes the graph with the initial packet data
    private void initializeGraph(Vector<Packet> packets) {
        currentSec = packets.firstElement().sec;
        int index = 0;
        if (drawTimescale) {
            // Time-based graph initialization
            while (index < packets.size()) {
                Packet p = packets.elementAt(index++);

                while (index < packets.size() && p.sec - currentSec <= count) {
                    staker.addPacket(p);
                    p = packets.elementAt(index++);
                }
                if (index == packets.size()) {
                    break;
                }
                currentSec += count;
                index--;
                lineGraph.addValue(staker.getValues(statType));
                staker.clear();
            }
        } else {
            // Packet count-based graph initialization
            Enumeration<?> en = packets.elements();
            while (en.hasMoreElements()) {
                for (int i = 0; en.hasMoreElements() && i < count; i++, currentCount++) {
                    staker.addPacket((Packet) en.nextElement());
                }
                if (!en.hasMoreElements()) {
                    break;
                }
                currentCount = 0;
                lineGraph.addValue(staker.getValues(statType));
                staker.clear();
            }
        }
    }

    // Adds a new packet and updates the statistics
    public void addPacket(Packet p) {
        staker.addPacket(p);
        if (drawTimescale) {
            handleTimeScale(p);
        } else {
            handlePacketCount();
        }
    }

    // Handles the timescale update for the graph
    private void handleTimeScale(Packet p) {
        if (currentSec == 0) {
            currentSec = p.sec;
        }
        if (p.sec - currentSec > count) {
            lineGraph.addValue(staker.getValues(statType));
            staker.clear();
            currentSec += count;
            if (p.sec - currentSec > count) {
                for (long s = p.sec - currentSec - count; s > count; s -= count) {
                    lineGraph.addValue(staker.getValues(statType));
                }
            }
        }
    }

    // Handles the packet count update for the graph
    private void handlePacketCount() {
        currentCount++;
        if (currentCount == count) {
            lineGraph.addValue(staker.getValues(statType));
            staker.clear();
            currentCount = 0;
        }
    }

    // Clears the statistics and the graph
    public void clear() {
        currentCount = 0;
        currentSec = 0;
        lineGraph.clear();
    }

    // Forces the UI to repaint
    void fireUpdate() {
        repaint();
    }
}
