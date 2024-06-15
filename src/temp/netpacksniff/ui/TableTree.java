package netpacksniff.ui;

import jpcap.packet.Packet;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import netpacksniff.PacketAnalyzerLoader;
import netpacksniff.analyzer.PacketAnalyzerAbstract;

import java.util.Vector;

class TableTree extends JComponent {
    private static final long serialVersionUID = 1L;

    private JTree tree;
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    private PacketAnalyzerAbstract[] analyzers = PacketAnalyzerLoader.getAnalyzers();

    public TableTree() {
        tree = new JTree(root);
        tree.setRootVisible(false);
        JScrollPane treeView = new JScrollPane(tree);

        setLayout(new BorderLayout());
        add(treeView, BorderLayout.CENTER);
    }

    public void analyzePacket(Packet packet) {
        boolean[] isExpanded = new boolean[root.getChildCount()];
        for (int i = 0; i < root.getChildCount(); i++) {
            isExpanded[i] = tree.isExpanded(new TreePath(((DefaultMutableTreeNode) root.getChildAt(i)).getPath()));
        }

        root.removeAllChildren();

        for (PacketAnalyzerAbstract analyzer : analyzers) {
            if (analyzer.isAnalyzable(packet)) {
                analyzer.analyze(packet);
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(analyzer.getProtocolName());
                root.add(node);
                String[] names = analyzer.getValueNames();
                Object[] values = analyzer.getValues();
                if (names == null) continue;

                for (int j = 0; j < names.length; j++) {
                    if (values[j] instanceof Vector) {
                        addNodes(node, names[j], (Vector<?>) values[j]);
                    } else if (values[j] != null) {
                        addNode(node, names[j] + ": " + values[j]);
                    }
                }
            }
        }
        ((DefaultTreeModel) tree.getModel()).nodeStructureChanged(root);

        for (int i = 0; i < Math.min(root.getChildCount(), isExpanded.length); i++) {
            if (isExpanded[i]) tree.expandPath(new TreePath(((DefaultMutableTreeNode) root.getChildAt(i)).getPath()));
        }
    }

    private void addNode(DefaultMutableTreeNode node, String str) {
        node.add(new DefaultMutableTreeNode(str));
    }

    private void addNodes(DefaultMutableTreeNode node, String str, Vector<?> v) {
        DefaultMutableTreeNode subnode = new DefaultMutableTreeNode(str);

        for (Object obj : v) {
            subnode.add(new DefaultMutableTreeNode(obj));
        }

        node.add(subnode);
    }
}
