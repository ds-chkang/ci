package medousa.sequential.graph.funnel;

import medousa.MyProgressBar;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Set;

public class MyFunnelViewerMenu
extends JPopupMenu
implements ActionListener {

    protected MyFunnelGraphViewer funnelGraphViewer;
    private JMenuItem addNodeMenuItem = new JMenuItem("ADD NODE");
    private JMenuItem removeAll = new JMenuItem("REMOVE ALL");

    public MyFunnelViewerMenu(MyFunnelGraphViewer funnelGraphViewer) {
        this.funnelGraphViewer = funnelGraphViewer;
        decorate();
    }

    private void decorate() {
        this.setMenuItem(this.addNodeMenuItem);
        this.add(new JSeparator());
        this.setMenuItem(this.removeAll);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        this.add(menuItem);
    }

    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addNodeMenuItem) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (funnelGraphViewer.funnelAnalysisApp.funnelGraph.getVertexCount() == 0) {
                        showNodeList();
                    }
                }
            }).start();
        } else if (e.getSource() == this.removeAll) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (funnelGraphViewer.funnelAnalysisApp.funnelGraph.getVertexCount() > 0) {
                        MyProgressBar pb = new MyProgressBar(false);
                        int pbCnt = 0;
                        Collection<MyNode> nodes = funnelGraphViewer.funnelAnalysisApp.funnelGraph.vRefs.values();
                        for (MyNode n : nodes) {
                            funnelGraphViewer.funnelAnalysisApp.funnelGraph.removeVertex(n);
                            pb.updateValue(++pbCnt, nodes.size());
                        }
                        funnelGraphViewer.funnelAnalysisApp.funnelGraph.vRefs.clear();
                        funnelGraphViewer.revalidate();
                        funnelGraphViewer.repaint();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                }
            }).start();
        }
    }

    private void showNodeList() {
        JFrame f = new JFrame("NODE LIST");

        String [] columns = {"NO.", "NODE"};
        String [][] data = {};
        DefaultTableModel m = new DefaultTableModel(data, columns);
        JTable table = new JTable(m);
        table.setBackground(Color.WHITE);
        table.setRowHeight(23);
        table.setFont(MySequentialGraphVars.f_pln_12);
        table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getTableHeader().setOpaque(false);
        table.getTableHeader().setBackground(new Color(0,0,0,0f));

        int i=0;
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            String nn = (n.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(n.getName()) :
                    MySequentialGraphSysUtil.getDecodedNodeName(n.getName()));
            m.addRow(new String[]{"" + (++i), nn});
        }

        JTextField searchTxt = new JTextField();
        searchTxt.setBorder(BorderFactory.createEtchedBorder());
        searchTxt.setFont(MySequentialGraphVars.f_pln_12);

        JButton addBtn = new JButton("ADD");
        addBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String node = table.getValueAt(table.getSelectedRow(), 1).toString();
                node = MySequentialGraphVars.nodeNameMap.get(node);
                if (funnelGraphViewer.funnelAnalysisApp.funnelGraph.vRefs.containsKey(node)) {
                    MyMessageUtil.showInfoMsg(funnelGraphViewer.funnelAnalysisApp, "");
                } else {
                    MyNode n = new MyNode(node);
                    funnelGraphViewer.getGraphLayout().setLocation(
                            n, new Point2D.Double(
                                    funnelGraphViewer.mouseClickedLocation.getX()+100,
                                    funnelGraphViewer.mouseClickedLocation.getY()+150));
                    funnelGraphViewer.funnelAnalysisApp.funnelGraph.addVertex(n);
                    funnelGraphViewer.funnelAnalysisApp.funnelGraph.addVertex(n);
                    funnelGraphViewer.funnelAnalysisApp.funnelGraph.vRefs.put(node, n);
                    f.dispose();
                    funnelGraphViewer.revalidate();
                    funnelGraphViewer.repaint();
                }
            }
        });

        f.setLayout(new BorderLayout(3,3));
        f.getContentPane().add(MyTableUtil.searchAndAddNodePanelForJTable(this, searchTxt, addBtn, m, table), BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(350, 500));
        f.setMaximumSize(new Dimension(500, 600));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.pack();
        f.setVisible(true);
    }
}
