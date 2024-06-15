/**
 * Author: Aditya
 */

package netpacksniff.analyzer;

import jpcap.packet.*;

public class EthernetAnalyzer extends PacketAnalyzerAbstract {
    private static final String[] valueNames = {
        "Frame Type",
        "Source MAC",
        "Destination MAC"
    };
    
    private EthernetPacket eth;

    public EthernetAnalyzer() {
        layer = DATALINK_LAYER;
    }

    public boolean isAnalyzable(Packet p) {
        return (p.datalink != null && p.datalink instanceof EthernetPacket);
    }

    @Override
    public String getProtocolName() {
        return "Ethernet Frame";
    }

    @Override
    public String[] getValueNames() {
        return valueNames;
    }

    @Override
    public void analyze(Packet p) {
        if (!isAnalyzable(p))
            return;
        eth = (EthernetPacket) p.datalink;
    }

    @Override
    public Object getValue(String valueName) {
        for (int i = 0; i < valueNames.length; i++) {
            if (valueNames[i].equals(valueName)) {
                return getValueAt(i);
            }
        }
        return null;
    }

    public Object getValueAt(int index) {
        switch (index) {
            case 0:
                return eth.frametype;
            case 1:
                return eth.getSourceAddress();
            case 2:
                return eth.getDestinationAddress();
            default:
                return null;
        }
    }

    @Override
    public Object[] getValues() {
        Object[] v = new Object[3];
        for (int i = 0; i < 3; i++) {
            v[i] = getValueAt(i);
        }
        return v;
    }
}
