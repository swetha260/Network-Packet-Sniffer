package netpacksniff.stat;

import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import java.util.Vector;

import netpacksniff.PacketAnalyzerLoader;
import netpacksniff.analyzer.PacketAnalyzerAbstract;

public class ApplicationProtocolStat extends StatisticsTaker {
    private PacketAnalyzerAbstract[] analyzers;
    private long[] numOfPs;
    private long[] sizeOfPs;
    private long totalPs;
    private long totalSize;
    private String[] labels;
    private static final String[] types = {"# of packets", "% of packets", "total packet size", "% of size"};

    public ApplicationProtocolStat() {
        analyzers = PacketAnalyzerLoader.getAnalyzersOf(PacketAnalyzerAbstract.APPLICATION_LAYER);
        numOfPs = new long[analyzers.length + 1];
        sizeOfPs = new long[analyzers.length + 1];

        labels = new String[analyzers.length + 1];
        for (int i = 0; i < analyzers.length; i++) {
            labels[i] = analyzers[i].getProtocolName();
        }
        labels[analyzers.length] = "Other";
    }

    @Override
    public String getName() {
        return "Application Layer Protocol Ratio";
    }

    @Override
    public void analyze(Vector<Packet> packets) {
        for (int i = 0; i < packets.size(); i++) {
            Packet p = packets.elementAt(i);
            totalPs++;
            
            boolean analyzed = false;
            for (int j = 0; j < analyzers.length; j++) {
                if (analyzers[j].isAnalyzable(p)) {
                    numOfPs[j]++;
                    sizeOfPs[j] += ((IPPacket) p).length;
                    totalSize += ((IPPacket) p).length;
                    analyzed = true;
                    break;
                }
            }
            if (!analyzed) {
                numOfPs[numOfPs.length - 1]++;
                sizeOfPs[sizeOfPs.length - 1] += p.len - 12; // 12 bytes for Ethernet header
                totalSize += p.len - 12;
            }
        }
    }

    @Override
    public void addPacket(Packet p) {
        boolean analyzed = false;
        totalPs++;
        for (int j = 0; j < analyzers.length; j++) {
            if (analyzers[j].isAnalyzable(p)) {
                numOfPs[j]++;
                sizeOfPs[j] += ((IPPacket) p).length;
                totalSize += ((IPPacket) p).length;
                analyzed = true;
                break;
            }
        }
        if (!analyzed) {
            numOfPs[numOfPs.length - 1]++;
            sizeOfPs[sizeOfPs.length - 1] += p.len - 12; // 12 bytes for Ethernet header
            totalSize += p.len - 12;
        }
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
        switch (index) {
            case 0: // # of packets
                return numOfPs != null ? numOfPs : new long[0];
            case 1: // % of packets
                return calculatePercentage(numOfPs, totalPs);
            case 2: // total packet size
                return sizeOfPs != null ? sizeOfPs : new long[0];
            case 3: // % of size
                return calculatePercentage(sizeOfPs, totalSize);
            default:
                return null;
        }
    }

    private long[] calculatePercentage(long[] values, long total) {
        long[] percentages = new long[values.length];
        if (total == 0) return percentages;
        for (int i = 0; i < values.length; i++) {
            percentages[i] = values[i] * 100 / total;
        }
        return percentages;
    }

    @Override
    public void clear() {
        numOfPs = new long[analyzers.length + 1];
        sizeOfPs = new long[analyzers.length + 1];
        totalPs = 0;
        totalSize = 0;
    }
}
