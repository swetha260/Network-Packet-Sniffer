package netpacksniff.analyzer;

import jpcap.packet.*;
import java.util.*;

public class IPv4Analyzer extends PacketAnalyzerAbstract {
    private static final String[] valueNames = {
        "Version",
        "TOS: Priority",
        "TOS: Throughput",
        "TOS: Reliability",
        "Length",
        "Identification",
        "Fragment: Don't Fragment",
        "Fragment: More Fragment",
        "Fragment Offset",
        "Time To Live",
        "Protocol",
        "Source IP",
        "Destination IP"
    };

    private Map<String, Object> values = new HashMap<>();

    public IPv4Analyzer() {
        layer = NETWORK_LAYER;
    }

    public boolean isAnalyzable(Packet p) {
        return p instanceof IPPacket && ((IPPacket) p).version == 4;
    }

    public String getProtocolName() {
        return "IPv4";
    }

    public String[] getValueNames() {
        return valueNames;
    }

    public void analyze(Packet packet) {
        values.clear();
        if (!isAnalyzable(packet)) return;
        IPPacket ip = (IPPacket) packet;
        values.put(valueNames[0], 4);
        values.put(valueNames[1], ip.priority);
        values.put(valueNames[2], ip.t_flag);
        values.put(valueNames[3], ip.r_flag);
        values.put(valueNames[4], ip.length);
        values.put(valueNames[5], ip.ident);
        values.put(valueNames[6], ip.dont_frag);
        values.put(valueNames[7], ip.more_frag);
        values.put(valueNames[8], ip.offset);
        values.put(valueNames[9], ip.hop_limit);
        values.put(valueNames[10], ip.protocol);
        values.put(valueNames[11], ip.src_ip.getHostAddress());
        values.put(valueNames[12], ip.dst_ip.getHostAddress());
    }

    public Object getValue(String valueName) {
        return values.get(valueName);
    }

    public Object getValueAt(int index) {
        if (index < 0 || index >= valueNames.length) return null;
        return values.get(valueNames[index]);
    }

    public Object[] getValues() {
        Object[] v = new Object[valueNames.length];
        for (int i = 0; i < valueNames.length; i++)
            v[i] = getValueAt(i);
        return v;
    }
}
