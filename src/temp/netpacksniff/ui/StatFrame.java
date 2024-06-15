package netpacksniff.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jpcap.packet.Packet;
import javax.swing.*;

public abstract class StatFrame extends JFrame {

    private static final long serialVersionUID = 1L; // Added serialVersionUID

    private Timer JDStatFrameUpdater;

    public StatFrame(String title) {
        super(title);
        JDStatFrameUpdater = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                fireUpdate();
                repaint();
            }
        });
        
        JDStatFrameUpdater.start();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                setVisible(false);
            }
        });
    }

    abstract void fireUpdate();
    public abstract void addPacket(Packet p);
    public abstract void clear();

    public void startUpdating() {
        JDStatFrameUpdater.setRepeats(true);
        JDStatFrameUpdater.start();
    }

    public void stopUpdating() {
        JDStatFrameUpdater.stop();
        JDStatFrameUpdater.setRepeats(false);
        JDStatFrameUpdater.start();
    }
}
