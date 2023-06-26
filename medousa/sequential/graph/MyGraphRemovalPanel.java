package medousa.sequential.graph;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import medousa.MyProgressBar;
import medousa.direct.graph.MyDirectGraphComboBoxTooltipper;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.sequential.graph.stats.barchart.MyGraphLevelNodeValueBarChart;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Set;

public class MyGraphRemovalPanel
extends JPanel
implements ActionListener {

    public JComboBox graphFilterComboBoxMenu = new JComboBox();
    public JComboBox graphFilterExclusionSymbolComboBoxMenu = new JComboBox();
    public JTextField thresholdTxt = new JTextField();
    public boolean isGraphFiltered;

    public MyGraphRemovalPanel() {
        decorate();
    }

    private void decorate() {
        final MyGraphRemovalPanel graphFilterPanel = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setBackground(Color.WHITE);
                setLayout(new BorderLayout(3,3));
                setPreferredSize(new Dimension(200, 60));
                setToolTipText("REMOVE GRAPHS BY FILTERING BY THE NUMBER OF NODES");

                graphFilterComboBoxMenu.addItem("");
                graphFilterComboBoxMenu.addItem("R. G");
                graphFilterComboBoxMenu.setBackground(Color.WHITE);
                graphFilterComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont12);
                graphFilterComboBoxMenu.setFocusable(false);
                graphFilterComboBoxMenu.setToolTipText("SELECT A GRAPH FILTER");
                graphFilterComboBoxMenu.setPreferredSize(new Dimension(65, 24));
                graphFilterComboBoxMenu.setMaximumSize(new Dimension(65, 24));

                graphFilterExclusionSymbolComboBoxMenu.setToolTipText("SELECT AN ARITHMETIC SYMBOL");
                graphFilterExclusionSymbolComboBoxMenu.setBackground(Color.WHITE);
                graphFilterExclusionSymbolComboBoxMenu.setFont(MyDirectGraphVars.tahomaPlainFont11);
                graphFilterExclusionSymbolComboBoxMenu.setFocusable(false);
                graphFilterExclusionSymbolComboBoxMenu.addItem("");
                graphFilterExclusionSymbolComboBoxMenu.addItem("<");
                graphFilterExclusionSymbolComboBoxMenu.addItem(">");
                graphFilterExclusionSymbolComboBoxMenu.addItem("==");
                graphFilterExclusionSymbolComboBoxMenu.addItem("!=");
                graphFilterExclusionSymbolComboBoxMenu.addItem("<=");
                graphFilterExclusionSymbolComboBoxMenu.addItem(">=");
                graphFilterExclusionSymbolComboBoxMenu.addItem("BTW");
                //graphFilterExclusionSymbolComboBoxMenu.setPreferredSize(new Dimension(48, 25));
                graphFilterExclusionSymbolComboBoxMenu.addActionListener(graphFilterPanel);

                thresholdTxt.setBorder(BorderFactory.createEtchedBorder());
                thresholdTxt.setToolTipText("Provide a threshold for the number of nodes to remove small graphs.");
                thresholdTxt.setBackground(Color.WHITE);
                thresholdTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);
                thresholdTxt.setPreferredSize(new Dimension(60, 22));

                String [] graphFilterMenuTooltips = {"SELECT A GRAPH FILTER", "REMOVE GRAPH"};
                MyDirectGraphComboBoxTooltipper comboBoxTooltipper = new MyDirectGraphComboBoxTooltipper();
                comboBoxTooltipper.setTooltips(Arrays.asList(graphFilterMenuTooltips));
                graphFilterComboBoxMenu.setRenderer(comboBoxTooltipper);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2,2));
                bottomPanel.setBackground(Color.WHITE);

               // bottomPanel.add(graphFilterComboBoxMenu);
                bottomPanel.add(thresholdTxt);
                bottomPanel.add(graphFilterExclusionSymbolComboBoxMenu);
                add(bottomPanel, BorderLayout.CENTER);

                TitledBorder border = BorderFactory.createTitledBorder("");
                border.setTitleFont(MyDirectGraphVars.tahomaBoldFont11);
                setBorder(BorderFactory.createTitledBorder(border, "GRAPH EXCL.", TitledBorder.LEFT, TitledBorder.TOP));
            }
        });
    }

    @Override public void actionPerformed(ActionEvent ae) {
        final MyGraphRemovalPanel graphRemovalPanel = this;
        if (graphFilterExclusionSymbolComboBoxMenu == ae.getSource()) {
            new Thread(new Runnable() {
                @Override public void run() {
                    if (thresholdTxt.getText().length() == 0) {
                        MyMessageUtil.showInfoMsg("Provide a threshold for nodes.");
                        return;
                    }

                    if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 0 &&
                        thresholdTxt.getText().length() > 0) {
                        MyMessageUtil.showInfoMsg("Select an arithmetic symbol.");
                        return;
                    }

                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        WeakComponentClusterer<MyNode, MyEdge> wcSearch = new WeakComponentClusterer<>();
                        Set<Set<MyNode>> clusters = wcSearch.transform(MySequentialGraphVars.g);
                        int removeGraphCnt = 0;
                        int removedNodeCnt = 0;
                        if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 1) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (cluster.size() > threshold) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 2) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (cluster.size() < threshold) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 3) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (cluster.size() == threshold) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 4) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (cluster.size() != threshold) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 5) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (threshold <= cluster.size()) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 6) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (threshold >= cluster.size()) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        } else if (graphFilterExclusionSymbolComboBoxMenu.getSelectedIndex() == 7) {
                            int threshold = Integer.parseInt(thresholdTxt.getText().replaceAll(" ", ""));
                            int i = 0;
                            for (Set<MyNode> cluster : clusters) {
                                if (cluster.size() >= threshold || cluster.size() <= threshold) {
                                    for (MyNode vertex : cluster) {
                                        vertex.setCurrentValue(0);
                                        //MySequentialGraphVars.g.removeVertex(vertex);
                                        removedNodeCnt++;
                                    }
                                    removeGraphCnt++;
                                }
                                pb.updateValue((++i), clusters.size());
                            }
                        }

                        //MySequentialGraphVars.getSequentialGraphViewer().vc.vTxtStat.setTextStatistics();
                        MySequentialGraphVars.getSequentialGraphViewer().vc.updateTableInfos();
                        if (MySequentialGraphVars.getSequentialGraphViewer().vc.nodeValueBarChart.isSelected()) {
                            MySequentialGraphVars.getSequentialGraphViewer().remove(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
                            MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart = new MyGraphLevelNodeValueBarChart();
                            MySequentialGraphVars.getSequentialGraphViewer().add(MySequentialGraphVars.getSequentialGraphViewer().graphLevelNodeValueBarChart);
                        }

                        graphFilterComboBoxMenu.removeActionListener(graphRemovalPanel);
                        graphFilterComboBoxMenu.setSelectedIndex(0);
                        graphFilterComboBoxMenu.addActionListener(graphRemovalPanel);
                        thresholdTxt.setText("");

                        isGraphFiltered = true;
                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                        pb.updateValue(100, 100);
                        pb.dispose();

                        MyMessageUtil.showInfoMsg(
                                "<html><body>" +
                                        "<br>NO. OF REMOVED NDOES: " + MyMathUtil.getCommaSeperatedNumber(removedNodeCnt) +
                                        "<br>NO. OF REMOVED GRAPHS: " + MyMathUtil.getCommaSeperatedNumber(removeGraphCnt) +
                                        "</body></html>");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                }
                }).start();
            }
        }
}
