/**
 * Author: Aditya
 */

package netpacksniff.analyzer;

import jpcap.packet.*;

public class PacketAnalyzer extends PacketAnalyzerAbstract {
    private static final String[] valueNames = {
        "Captured Time",
        "Captured Length"
    };
    
    private Packet packet;

    @Override
    public boolean isAnalyzable(Packet packet) {
        return true; // All packets are analyzable by this analyzer
    }

    @Override
    public String getProtocolName() {
        return "Packet Information";
    }

    @Override
    public String[] getValueNames() {
        return valueNames;
    }

    @Override
    public void analyze(Packet p) {
        packet = p;
    }

    @Override
    public Object getValue(String name) {
        if (name.equals(valueNames[0])) {
            return new java.util.Date(packet.sec * 1000 + packet.usec / 1000).toString();
        } else if (name.equals(valueNames[1])) {
            return packet.caplen;
        } else {
            return null;
        }
    }

    public Object getValueAt(int index) {
        switch (index) {
            case 0:
                return new java.util.Date(packet.sec * 1000 + packet.usec / 1000).toString();
            case 1:
                return packet.caplen;
            default:
                return null;
        }
    }

    @Override
    public Object[] getValues() {
        Object[] v = new Object[2];
        v[0] = new java.util.Date(packet.sec * 1000 + packet.usec / 1000).toString();
        v[1] = packet.caplen;
        return v;
    }
}
