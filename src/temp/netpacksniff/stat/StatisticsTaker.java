/* 
 * This is an abstract class. All the other classes in this package extend this class 
 */

package netpacksniff.stat;

import java.util.Vector;
import jpcap.packet.Packet;

public abstract class StatisticsTaker {
    
    // Gets the name of the statistics taker
    public abstract String getName();

    // Analyzes the given packets to compute statistics
    public abstract void analyze(Vector<Packet> packets);

    // Adds a packet to the statistics computation
    public abstract void addPacket(Packet p);

    // Retrieves labels for the computed statistics
    public abstract String[] getLabels();

    // Retrieves types of statistics available
    public abstract String[] getStatTypes();

    // Retrieves values for a specific statistic type
    public abstract long[] getValues(int index);

    // Clears all computed statistics
    public abstract void clear();

    // Creates a new instance of the StatisticsTaker subclass
    public StatisticsTaker newInstance() {
        try {
            return this.getClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
