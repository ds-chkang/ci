package medousa.sequential.graph.funnel;

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
import java.util.HashSet;
import java.util.Set;

public class MyFunnelGraphNodeMenu
extends JPopupMenu
implements ActionListener {
    protected MyFunnelGraphViewer funnelGraphViewer;
    private JMenuItem addSuccessor = new JMenuItem("ADD SUCCESSOR");
    private JMenuItem addPredecessor = new JMenuItem("ADD PREDECESSOR");
    private JMenuItem deleteMenuItem = new JMenuItem("DELETE NODE");

    public MyFunnelGraphNodeMenu(MyFunnelGraphViewer funnelGraphViewer) {
        this.funnelGraphViewer = funnelGraphViewer;
        this.decorate();
    }

    private void decorate() {
        this.setMenuItem(this.addPredecessor);
        this.add(new JSeparator());
        this.setMenuItem(this.addSuccessor);
        this.add(new JSeparator());
        this.setMenuItem(this.deleteMenuItem);
    }

    private void setMenuItem(JMenuItem menuItem) {
        menuItem.setFont(MySequentialGraphVars.tahomaPlainFont12);
        menuItem.addActionListener(this);
        menuItem.setPreferredSize(new Dimension(130, 24));
        this.add(menuItem);
    }

    private void showPredecessorList() {
        JFrame f = new JFrame("PREDECESSOR LIST");

        String [] columns = {"NO.", "PREDECESSOR"};
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

        Set<String> predecessors = new HashSet<>();
        for (int s=0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i=0; i < MySequentialGraphVars.seqs[s].length; i++) {

            }
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.addPredecessor) {

        } else if (e.getSource() == this.addSuccessor) {

        } else if (e.getSource() == this.deleteMenuItem) {
            new Thread(new Runnable() {
                @Override public void run() {
                    try {
                        Thread.sleep(450);

                        funnelGraphViewer.funnelAnalysisApp.funnelGraph.removeVertex(
                                funnelGraphViewer.funnelGraphMouseListener.selectedNode
                        );

                        funnelGraphViewer.funnelAnalysisApp.funnelGraph.vRefs.remove(
                                funnelGraphViewer.funnelGraphMouseListener.selectedNode.getName()
                        );

                        funnelGraphViewer.funnelGraphMouseListener.selectedNode = null;
                        funnelGraphViewer.revalidate();
                        funnelGraphViewer.repaint();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
