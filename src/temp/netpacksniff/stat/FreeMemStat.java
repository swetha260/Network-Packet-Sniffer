package netpacksniff.stat;

import java.util.Vector;

import jpcap.packet.Packet;

public class FreeMemStat extends StatisticsTaker {
    // Labels for the statistics
    private static final String[] labels = {"Free Memory"};
    // Types of statistics
    private static final String[] types = {"Bytes"};

    @Override
    public String getName() {
        return "Free Memory";
    }

    @Override
    public void analyze(Vector<Packet> packets) {
        // No implementation needed for this class as it does not analyze packets
    }

    @Override
    public void addPacket(Packet p) {
        // No implementation needed for this class as it does not process individual packets
    }

    @Override
    public String[] getLabels() {
        return labels;
    }

    @Override
    public String[] getStatTypes() {
        return types;
    }

    @Override
    public long[] getValues(int index) {
        long[] ret = new long[1];
        ret[0] = Runtime.getRuntime().freeMemory();
        return ret;
    }

    @Override
    public void clear() {
        // No implementation needed for this class as there is no state to clear
    }
}
