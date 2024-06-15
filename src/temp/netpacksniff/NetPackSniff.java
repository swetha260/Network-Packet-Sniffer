package netpacksniff;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JOptionPane;

import netpacksniff.ui.Frame;


public class NetPackSniff {
    public static Properties JDProperty;

    public static javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();

    static Vector<Frame> frames = new Vector<>();

    public static void main(String[] args) {
    	try {
            Class.forName("jpcap.JpcapCaptor");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Cannot find Jpcap. Please download and install Jpcap before running.");
            System.exit(0);
        }

        PacketAnalyzerLoader.loadDefaultAnalyzer();
        StatisticsTakerLoader.loadStatisticsTaker();
        loadProperty();

        openNewWindow();
    }

    public static void saveProperty() {
        if (JDProperty == null) return;
        try {
            JDProperty.store(new FileOutputStream("JpcapDumper.property"), "JpcapDumper");
        } catch (IOException e) {
            // Handle exceptions if necessary
        }
    }

    static void loadProperty() {
        try {
            JDProperty = new Properties();
            JDProperty.load(new FileInputStream("JpcapDumper.property"));
        } catch (IOException e) {
            // Handle exceptions if necessary
        }
    }

    public static void openNewWindow() {
        Captor captor = new Captor();
        frames.add(Frame.openNewWindow(captor));
    }

    public static void closeWindow(Frame frame) {
        frame.captor.stopCapture();
        frame.captor.saveIfNot();
        frame.captor.closeAllWindows();
        frames.remove(frame);
        frame.dispose();
        if (frames.isEmpty()) {
            saveProperty();
            System.exit(0);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        saveProperty();
    }
}