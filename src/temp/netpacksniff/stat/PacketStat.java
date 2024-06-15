package netpacksniff.stat;

import jpcap.packet.Packet;
import java.util.Date;
import java.util.Vector;

public class PacketStat extends StatisticsTaker {
    static final String[] types = {
        "Total packet #",
        "Total packet size",
        "Average packet size",
        "bits/s",
        "pkts/s"
    };
    static final String[] label = {"Value"};

    private long numOfPs = 0;
    private long sizeOfPs = 0;
    private Date first = null;
    private Date last = null;

    @Override
    public String getName() {
        return "Overall information";
    }

    @Override
    public void analyze(Vector<Packet> packets) {
        if (packets.size() > 0) {
            Packet firstPacket = packets.firstElement();
            Packet lastPacket = packets.lastElement();
            first = new Date(firstPacket.sec * 1000L + firstPacket.usec / 1000);
            last = new Date(lastPacket.sec * 1000L + lastPacket.usec / 1000);
        }

        for (Packet packet : packets) {
            numOfPs++;
            sizeOfPs += packet.len;
        }
    }

    @Override
    public void addPacket(Packet p) {
        if (first == null) {
            first = new Date(p.sec * 1000L + p.usec / 1000);
        }
        last = new Date(p.sec * 1000L + p.usec / 1000);

        numOfPs++;
        sizeOfPs += p.len;
    }

    @Override
    public String[] getLabels() {
        return label;
    }

    @Override
    public String[] getStatTypes() {
        return types;
    }

    @Override
    public long[] getValues(int index) {
        long[] ret = new long[1];
        switch (index) {
            case 0: // Total packet #
                ret[0] = numOfPs;
                break;
            case 1: // Total packet size
                ret[0] = sizeOfPs;
                break;
            case 2: // Average packet size
                ret[0] = numOfPs == 0 ? 0 : sizeOfPs / numOfPs;
                break;
            case 3: // bits/s
            case 4: // pkts/s
                if (first == null || last == null) {
                    ret[0] = 0;
                } else {
                    long durationInSeconds = (last.getTime() - first.getTime()) / 1000;
                    if (durationInSeconds == 0) {
                        ret[0] = 0;
                    } else {
                        if (index == 3) {
                            ret[0] = (sizeOfPs * 8) / durationInSeconds;
                        } else {
                            ret[0] = numOfPs / durationInSeconds;
                        }
                    }
                }
                break;
            default:
                return null;
        }
        return ret;
    }

    @Override
    public void clear() {
        numOfPs = 0;
        sizeOfPs = 0;
        first = null;
        last = null;
    }
}
