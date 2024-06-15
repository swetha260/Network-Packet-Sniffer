package netpacksniff;

import java.util.*;

import netpacksniff.analyzer.ARPAnalyzer;
import netpacksniff.analyzer.EthernetAnalyzer;
import netpacksniff.analyzer.FTPAnalyzer;
import netpacksniff.analyzer.HTTPAnalyzer;
import netpacksniff.analyzer.ICMPAnalyzer;
import netpacksniff.analyzer.IPv4Analyzer;
import netpacksniff.analyzer.IPv6Analyzer;
import netpacksniff.analyzer.POP3Analyzer;
import netpacksniff.analyzer.PacketAnalyzer;
import netpacksniff.analyzer.PacketAnalyzerAbstract;
import netpacksniff.analyzer.SMTPAnalyzer;
import netpacksniff.analyzer.SSHAnalyzer;
import netpacksniff.analyzer.TCPAnalyzer;
import netpacksniff.analyzer.TelnetAnalyzer;
import netpacksniff.analyzer.UDPAnalyzer;

public class PacketAnalyzerLoader {
    static Vector<PacketAnalyzerAbstract> analyzers = new Vector<>();

    static void loadDefaultAnalyzer() {
        analyzers.addElement(new PacketAnalyzer());
        analyzers.addElement(new EthernetAnalyzer());
        analyzers.addElement(new IPv4Analyzer());
        analyzers.addElement(new IPv6Analyzer());
        analyzers.addElement(new TCPAnalyzer());
        analyzers.addElement(new UDPAnalyzer());
        analyzers.addElement(new ICMPAnalyzer());
        analyzers.addElement(new HTTPAnalyzer());
        analyzers.addElement(new FTPAnalyzer());
        analyzers.addElement(new TelnetAnalyzer());
        analyzers.addElement(new SSHAnalyzer());
        analyzers.addElement(new SMTPAnalyzer());
        analyzers.addElement(new POP3Analyzer());
        analyzers.addElement(new ARPAnalyzer());
    }

    public static PacketAnalyzerAbstract[] getAnalyzers() {
        PacketAnalyzerAbstract[] array = new PacketAnalyzerAbstract[analyzers.size()];

        for (int i = 0; i < array.length; i++)
            array[i] = analyzers.elementAt(i);

        return array;
    }

    public static PacketAnalyzerAbstract[] getAnalyzersOf(int layer) {
        Vector<PacketAnalyzerAbstract> v = new Vector<>();

        for (int i = 0; i < analyzers.size(); i++)
            if (analyzers.elementAt(i).layer == layer)
                v.addElement(analyzers.elementAt(i));

        PacketAnalyzerAbstract[] res = new PacketAnalyzerAbstract[v.size()];
        for (int i = 0; i < res.length; i++)
            res[i] = v.elementAt(i);

        return res;
    }
}
