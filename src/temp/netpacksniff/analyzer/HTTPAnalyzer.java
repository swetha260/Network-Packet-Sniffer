/* Author Aditya */

package netpacksniff.analyzer;

import jpcap.packet.*;
import java.util.*;
import java.io.*;

public class HTTPAnalyzer extends PacketAnalyzerAbstract {

    private static final String[] valueNames = {
        "Method",
        "Header"
    };
    private String method;
    private Vector<String> headers = new Vector<>();

    public HTTPAnalyzer() {
        layer = APPLICATION_LAYER;
    }

    public boolean isAnalyzable(Packet p) {
        if (p instanceof TCPPacket &&
           (((TCPPacket) p).src_port == 80 || ((TCPPacket) p).dst_port == 80)) {
            return true;
        } else {
            return false;
        }
    }

    public String getProtocolName() {
        return "HTTP";
    }

    public String[] getValueNames() {
        return valueNames;
    }

    public void analyze(Packet p) {
        method = "";
        headers.clear();
        if (!isAnalyzable(p)) {
            return;
        }

        try {
            BufferedReader in = new BufferedReader(new StringReader(new String(p.data)));

            method = in.readLine();
            if (method == null || method.indexOf("HTTP") == -1) {
                method = "Not HTTP Header";
                return;
            }

            String line;
            // Read headers
            while ((line = in.readLine()) != null && line.length() > 0) {
                headers.addElement(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getValue(String valueName) {
        if (valueNames[0].equals(valueName)) {
            return method;
        }
        if (valueNames[1].equals(valueName)) {
            return headers;
        }
        return null;
    }

    public Object[] getValues() {
        Object[] values = new Object[2];
        values[0] = method;
        values[1] = headers;

        return values;
    }

    @Override
    public Object getValueAt(int index) {
        switch (index) {
            case 0:
                return method;
            case 1:
                return headers;
            default:
                return null;
        }
    }
}
