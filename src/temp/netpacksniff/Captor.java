/* Author Aditya */

package netpacksniff;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jpcap.JpcapCaptor;
import jpcap.JpcapWriter;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;
import netpacksniff.stat.StatisticsTaker;
import netpacksniff.ui.CaptureDialog;
import netpacksniff.ui.ContinuousStatFrame;
import netpacksniff.ui.CumulativeStatFrame;
import netpacksniff.ui.Frame;
import netpacksniff.ui.StatFrame;

public class Captor {
    private static final long MAX_PACKETS_HOLD = 10000L;

    private Vector<Packet> packets = new Vector<>();
    private JpcapCaptor jpcap = null;

    private boolean isLiveCapture;
    private boolean isSaved = false;

    private Frame frame;
    private Thread captureThread;
    private Vector<StatFrame> sframes = new Vector<>();

    // Sets the main application frame
    public void setJDFrame(Frame frame) {
        this.frame = frame;
    }

    // Returns the captured packets
    public Vector<Packet> getPackets() {
        return packets;
    }

    // Captures packets from a selected network device
    public void capturePacketsFromDevice() {
        if (jpcap != null) {
            jpcap.close();
        }
        jpcap = CaptureDialog.getJpcap(frame);
        clear();

        if (jpcap != null) {
            isLiveCapture = true;
            frame.disableCapture();
            startCaptureThread();
        }
    }

    // Loads packets from a file
    public void loadPacketsFromFile() {
        isLiveCapture = false;
        clear();

        int ret = NetPackSniff.chooser.showOpenDialog(frame);
        if (ret == JFileChooser.APPROVE_OPTION) {
            String path = NetPackSniff.chooser.getSelectedFile().getPath();
            try {
                if (jpcap != null) {
                    jpcap.close();
                }
                jpcap = JpcapCaptor.openFile(path);
            } catch (java.io.IOException e) {
                JOptionPane.showMessageDialog(frame, "Can't open file: " + path);
                e.printStackTrace();
                return;
            }

            frame.disableCapture();
            startCaptureThread();
        }
    }

    // Clears all packets and resets the frames
    private void clear() {
        packets.clear();
        frame.clear();

        for (StatFrame statFrame : sframes) {
            statFrame.clear();
        }
    }

    // Saves captured packets to a file
    public void saveToFile() {
        if (packets == null) {
            return;
        }

        int ret = NetPackSniff.chooser.showSaveDialog(frame);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = NetPackSniff.chooser.getSelectedFile();

            if (file.exists()) {
                int overwrite = JOptionPane.showConfirmDialog(
                    frame,
                    "Overwrite " + file.getName() + "?",
                    "Overwrite?",
                    JOptionPane.YES_NO_OPTION
                );
                if (overwrite == JOptionPane.NO_OPTION) {
                    return;
                }
            }

            try {
                JpcapWriter writer = JpcapWriter.openDumpFile(jpcap, file.getPath());

                for (Packet packet : packets) {
                    writer.writePacket(packet);
                }

                writer.close();
                isSaved = true;
            } catch (java.io.IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Can't save file: " + file.getPath());
            }
        }
    }

    // Stops the current packet capture
    public void stopCapture() {
        stopCaptureThread();
    }

    // Prompts the user to save data if it hasn't been saved
    public void saveIfNot() {
        if (isLiveCapture && !isSaved) {
            int ret = JOptionPane.showConfirmDialog(
                null,
                "Save this data?",
                "Save this data?",
                JOptionPane.YES_NO_OPTION
            );
            if (ret == JOptionPane.YES_OPTION) {
                saveToFile();
            }
        }
    }

    // Adds a cumulative statistics frame
    public void addCumulativeStatFrame(StatisticsTaker taker) {
        sframes.add(CumulativeStatFrame.openWindow(packets, taker.newInstance()));
    }

    // Adds a continuous statistics frame
    public void addContinuousStatFrame(StatisticsTaker taker) {
        sframes.add(ContinuousStatFrame.openWindow(packets, taker.newInstance()));
    }

    // Closes all statistics frames
    public void closeAllWindows() {
        for (StatFrame statFrame : sframes) {
            statFrame.dispose();
        }
    }

    // Starts the packet capture thread
    private void startCaptureThread() {
        if (captureThread != null) {
            return;
        }

        captureThread = new Thread(() -> {
            while (captureThread != null) {
                if (jpcap.processPacket(1, handler) == 0 && !isLiveCapture) {
                    stopCaptureThread();
                }
                Thread.yield();
            }

            jpcap.breakLoop();
            frame.enableCapture();
        });
        captureThread.setPriority(Thread.MIN_PRIORITY);

        frame.startUpdating();
        for (StatFrame statFrame : sframes) {
            statFrame.startUpdating();
        }

        captureThread.start();
    }

    // Stops the packet capture thread
    private void stopCaptureThread() {
        captureThread = null;
        frame.stopUpdating();
        for (StatFrame statFrame : sframes) {
            statFrame.stopUpdating();
        }
    }

    // Packet receiver handler
    private final PacketReceiver handler = packet -> {
        packets.addElement(packet);
        while (packets.size() > MAX_PACKETS_HOLD) {
            packets.removeElementAt(0);
        }
        if (!sframes.isEmpty()) {
            for (StatFrame statFrame : sframes) {
                statFrame.addPacket(packet);
            }
        }
        isSaved = false;
    };
}
