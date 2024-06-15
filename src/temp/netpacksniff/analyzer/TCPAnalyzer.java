package netpacksniff.analyzer;

import jpcap.packet.*;
import java.util.*;

public class TCPAnalyzer extends PacketAnalyzerAbstract {
    private static final String[] valueNames = {
        "Source Port",
        "Destination Port",
        "Sequence Number",
        "Ack Number",
        "URG Flag",
        "ACK Flag",
        "PSH Flag",
        "RST Flag",
        "SYN Flag",
        "FIN Flag",
        "Window Size"
    };

    private Hashtable<String, Object> values = new Hashtable<>();

    public TCPAnalyzer() {
        layer = TRANSPORT_LAYER;
    }

    public boolean isAnalyzable(Packet p) {
        return (p instanceof TCPPacket);
    }

    public String getProtocolName() {
        return "TCP";
    }

    public String[] getValueNames() {
        return valueNames;
    }

    public void analyze(Packet p) {
        values.clear();
        if (!isAnalyzable(p)) return;
        TCPPacket tcp = (TCPPacket)p;
        values.put(valueNames[0], tcp.src_port);
        values.put(valueNames[1], tcp.dst_port);
        values.put(valueNames[2], tcp.sequence);
        values.put(valueNames[3], tcp.ack_num);
        values.put(valueNames[4], tcp.urg);
        values.put(valueNames[5], tcp.ack);
        values.put(valueNames[6], tcp.psh);
        values.put(valueNames[7], tcp.rst);
        values.put(valueNames[8], tcp.syn);
        values.put(valueNames[9], tcp.fin);
        values.put(valueNames[10], tcp.window);
    }

    public Object getValue(String valueName) {
        return values.get(valueName);
    }

    Object getValueAt(int index) {
        if (index < 0 || index >= valueNames.length) return null;
        return values.get(valueNames[index]);
    }

    public Object[] getValues() {
        Object[] v = new Object[valueNames.length];
        for (int i = 0; i < valueNames.length; i++) {
            v[i] = values.get(valueNames[i]);
        }
        return v;
    }
}
