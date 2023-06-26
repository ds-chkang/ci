package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyEdge;
import medousa.sequential.utils.MyMathUtil;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

public class MyEdgeStatistics
extends JPanel
implements ActionListener {

    private String [] timeConstrainedColumns = {
            "NO.",
            "PREDECESSOR",
            "SUCCESSOR",
            "UNIQ. CONT.",
            "CONT.",
            "REACH TIME",
            "SUPPORT",
            "CONFIDENCE",
            "LIFT",
    };
    private String [] columns = {
            "NO.",
            "PREDECESSOR",
            "SUCCESSOR",
            "UNIQ. CONT.",
            "CONT.",
            "SUPPORT",
            "CONFIDENCE",
            "LIFT"
    };

    private String [][] data = {};

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchTxt = new JTextField();
    private JButton saveBtn = new JButton(" SAVE ");
    private JComboBox distributionComboBox = new JComboBox();
    private JComboBox orderByComboBox = new JComboBox();
    private JFrame frame;

    public MyEdgeStatistics() {
        this.decorate();
        this.frame = new JFrame("EDGE SUMMARY STATISTICS");
        this.frame.setLayout(new BorderLayout(5,5));
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.getContentPane().add(this, BorderLayout.CENTER);
        this.frame.setPreferredSize(new Dimension(800, 600));
        this.frame.pack();
        this.frame.setLocation(MySequentialGraphSysUtil.getViewerWidth()-800, 5);
        this.frame.setVisible(true);
        this.frame.setAlwaysOnTop(true);
        this.frame.setAlwaysOnTop(false);
    }

    private void decorate() {
        MyProgressBar pb = new MyProgressBar(false);
        this.setLayout(new BorderLayout(5, 5));
        this.setBackground(Color.WHITE);
        pb.updateValue(10, 100);

        if (MySequentialGraphVars.isTimeOn) {this.setTimeConstrainedEdgeStatistics();
        } else {this.setEdgeStatistics();}

        JLabel distributionLabel = new JLabel("DISTRIBUTION:");
        distributionLabel.setBackground(Color.WHITE);
        distributionLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.distributionComboBox.setFocusable(false);
        this.distributionComboBox.setBackground(Color.WHITE);
        this.distributionComboBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.distributionComboBox.addItem("SELECT");
        this.distributionComboBox.addItem("CONTRIBUTION");
        this.distributionComboBox.addItem("UNIQUE CONTRIBUTION");
        this.distributionComboBox.addItem("SUPPORT");
        this.distributionComboBox.addItem("CONFIDENCE");
        this.distributionComboBox.addItem("LIFT");
        if (MySequentialGraphVars.isTimeOn) this.distributionComboBox.addItem("REACH TIME");
        this.distributionComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (MySequentialGraphVars.isTimeOn) {
                    if (distributionComboBox.getSelectedIndex() == 1) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE CONTRIBUTION DISTRIBUTION", table, 4, "CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 2) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE UNIQUE CONTRIBUTION DISTRIBUTION", table, 3, "UNIQUE CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 3) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE SUPPORT DISTRIBUTION", table, 6, "SUPPORT");
                    } else if (distributionComboBox.getSelectedIndex() == 4) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE CONFIDENCE DISTRIBUTION", table, 7, "CONFIDENCE");
                    } else if (distributionComboBox.getSelectedIndex() == 5) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE LIFT DISTRIBUTION", table, 8, "LIFT");
                    } else if (distributionComboBox.getSelectedIndex() == 6) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE REACH TIME DISTRIBUTION", table, 5, "REACH TIME");
                    }
                } else {
                    if (distributionComboBox.getSelectedIndex() == 1) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE CONTRIBUTION DISTRIBUTION", table, 4, "CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 2) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE UNIQUE CONTRIBUTION DISTRIBUTION", table, 3, "UNIQUE CONTRIBUTION");
                    } else if (distributionComboBox.getSelectedIndex() == 3) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE SUPPORT DISTRIBUTION", table, 6, "SUPPORT");
                    } else if (distributionComboBox.getSelectedIndex() == 4) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE CONFIDENCE DISTRIBUTION", table, 7, "CONFIDENCE");
                    } else if (distributionComboBox.getSelectedIndex() == 5) {
                        MyValueDistributionChartGenerator valueDistributionChart = new MyValueDistributionChartGenerator("EDGE LIFT DISTRIBUTION", table, 8, "LIFT");
                    }
                }
            }
        });

        JLabel orderByLabel = new JLabel("ORDER BY: ");
        orderByLabel.setBackground(Color.WHITE);
        orderByLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        this.orderByComboBox.setFocusable(false);
        this.orderByComboBox.setBackground(Color.WHITE);
        this.orderByComboBox.setFont(MySequentialGraphVars.tahomaPlainFont12);
        this.orderByComboBox.addItem("UNIQUE CONTRIBUTION");
        this.orderByComboBox.addItem("CONTRIBUTION");
        if (MySequentialGraphVars.isTimeOn) {this.orderByComboBox.addItem("REACH TIME");}
        this.orderByComboBox.addItem("SUPPORT");
        this.orderByComboBox.addItem("CONFIDENCE");
        this.orderByComboBox.addItem("LIFT");
        this.orderByComboBox.addActionListener(new ActionListener() {@Override
            public void actionPerformed(ActionEvent e) {new Thread(new Runnable() {@Override
                public void run() {
                    try {
                        if (MySequentialGraphVars.isTimeOn) {
                            frame.setAlwaysOnTop(true);
                            MyProgressBar pb = new MyProgressBar(false);
                            MySequentialGraphVars.edgeOrderByComboBoxIdx = orderByComboBox.getSelectedIndex();
                            for (int i = model.getRowCount() - 1; i >= 0; i--) {
                                model.removeRow(i);
                            }
                            pb.updateValue(40, 100);
                            ArrayList<MyEdge> edges = new ArrayList<>(MySequentialGraphVars.g.getEdges());
                            Collections.sort(edges);
                            Map<MyEdge, Long> edgeReachTimeMap = getEdgeReachTimes(edges);
                            pb.updateValue(70, 100);
                            int cnt = 0;
                            for (MyEdge e : edges) {
                                model.addRow(new String[]{
                                    String.valueOf(++cnt),
                                    MySequentialGraphSysUtil.getNodeName(e.getSource().getName()),
                                    MySequentialGraphSysUtil.getNodeName(e.getDest().getName()),
                                    MyMathUtil.getCommaSeperatedNumber(e.getUniqueContribution()),
                                    MyMathUtil.getCommaSeperatedNumber(e.getContribution()),
                                    MyMathUtil.getCommaSeperatedNumber(edgeReachTimeMap.get(e)),
                                    MyMathUtil.twoDecimalFormat(e.getSupport()),
                                    MyMathUtil.twoDecimalFormat(e.getConfidence()),
                                    MyMathUtil.twoDecimalFormat(e.getLift())});
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                            frame.setAlwaysOnTop(false);
                        } else {
                            frame.setAlwaysOnTop(true);
                            MyProgressBar pb = new MyProgressBar(false);
                            MySequentialGraphVars.edgeOrderByComboBoxIdx = orderByComboBox.getSelectedIndex();
                            for (int i = model.getRowCount() - 1; i >= 0; i--) {model.removeRow(i);}
                            pb.updateValue(40, 100);
                            ArrayList<MyEdge> edges = new ArrayList<>(MySequentialGraphVars.g.getEdges());
                            Collections.sort(edges);
                            pb.updateValue(70, 100);
                            int cnt = 0;
                            for (MyEdge e : edges) {
                                model.addRow(new String[]{
                                    String.valueOf(++cnt),
                                    MySequentialGraphSysUtil.getNodeName(e.getSource().getName()),
                                    MySequentialGraphSysUtil.getNodeName(e.getDest().getName()),
                                    MyMathUtil.getCommaSeperatedNumber(e.getUniqueContribution()),
                                    MyMathUtil.getCommaSeperatedNumber(e.getContribution()),
                                    MyMathUtil.twoDecimalFormat(e.getSupport()),
                                    MyMathUtil.twoDecimalFormat(e.getConfidence()),
                                    MyMathUtil.twoDecimalFormat(e.getLift())});
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                            frame.setAlwaysOnTop(false);
                        }
                    } catch (Exception ex) {ex.printStackTrace();}
                }}).start();
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(orderByLabel);
        rightPanel.add(orderByComboBox);
        rightPanel.add(distributionLabel);
        rightPanel.add(this.distributionComboBox);

        JLabel numberOfEdgeLabel = new JLabel("NO. OF EDGES: " + MyMathUtil.getCommaSeperatedNumber(this.table.getRowCount()));
        numberOfEdgeLabel.setBackground(Color.WHITE);
        numberOfEdgeLabel.setFont(MySequentialGraphVars.tahomaPlainFont12);

        JLabel selectedEdge = new JLabel("");
        selectedEdge.setFont(MySequentialGraphVars.f_pln_12);
        selectedEdge.setBackground(Color.WHITE);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                selectedEdge.setText("    SELECTED EDGE: " + table.getValueAt(table.getSelectedRow(), 1).toString() + " - " + table.getValueAt(table.getSelectedRow(), 2).toString());
            }
        });

        this.searchTxt.addKeyListener(new KeyAdapter() {@Override
            public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            numberOfEdgeLabel.setText("NO. OF EDGES: " + MyMathUtil.getCommaSeperatedNumber(table.getRowCount()));
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.WHITE);
        topPanel.setLayout(new GridLayout(1,2));
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(numberOfEdgeLabel);
        leftPanel.add(selectedEdge);
        topPanel.add(leftPanel);
        topPanel.add(rightPanel);
        JScrollPane tableScrollPane = new JScrollPane(this.table);
        tableScrollPane.setPreferredSize(new Dimension(1000, 650));
        this.add(topPanel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.searchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        JPanel searchAndSavePanel = MyTableUtil.searchAndSaveDataPanelForJTable2(this, searchTxt, this.saveBtn, this.model, this.table);
        this.saveBtn.setPreferredSize(new Dimension(70, 29));
        this.add(searchAndSavePanel, BorderLayout.SOUTH);
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setRowSorter(MyTableUtil.setJTableRowSorterWithTextField(this.model, this.searchTxt));
        this.table.setSelectionBackground(Color.BLACK);
        this.table.setSelectionForeground(Color.WHITE);
        this.table.setFocusable(false);
        this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        this.setPreferredSize(new Dimension(450, 700));
        pb.updateValue(100, 100);
        pb.dispose();
    }

    private Map<MyEdge, Integer> getEdgeContributions(Collection<MyEdge> edges) {
        Map<MyEdge, Integer> edgeContribtuionMap = new HashMap<>();
        for (MyEdge edge : edges) {
            int edgeValue = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String pred = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                    String suc = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (pred.equals(edge.getSource().getName()) && suc.equals(edge.getDest().getName())) {
                        edgeValue++;
                    }
                }
            }
            edgeContribtuionMap.put(edge, edgeValue);
        }
        return edgeContribtuionMap;
    }

    private Map<MyEdge, Integer> getEdgeUniqueContributions(Collection<MyEdge> edges) {
        Map<MyEdge, Integer> edgeUniqueContribtuionMap = new HashMap<>();
        for (MyEdge edge : edges) {
            int edgeValue = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String pred = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                    String suc = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (pred.equals(edge.getSource().getName()) && suc.equals(edge.getDest().getName())) {
                        edgeValue++;
                        break;
                    }
                }
            }
            edgeUniqueContribtuionMap.put(edge, edgeValue);
        }
        return edgeUniqueContribtuionMap;
    }

    private Map<MyEdge, Long> getEdgeReachTimes(Collection<MyEdge> edges) {
        Map<MyEdge, Long> edgeReachTimeMap = new HashMap<>();
        for (MyEdge edge : edges) {
            long edgeValue = 0;
            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                    String pred = MySequentialGraphVars.seqs[s][i-1].split(":")[0];
                    String suc = MySequentialGraphVars.seqs[s][i].split(":")[0];
                    if (pred.equals(edge.getSource().getName()) && suc.equals(edge.getDest().getName())) {
                        edgeValue += Float.valueOf(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                    }
                }
            }
            edgeReachTimeMap.put(edge, edgeValue);
        }
        return edgeReachTimeMap;
    }

    private void setTimeConstrainedEdgeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.timeConstrainedColumns);
            ArrayList<MyEdge> edges = new ArrayList<>(MySequentialGraphVars.g.getEdges());
            Collections.sort(edges);

            Map<MyEdge, Integer> edgeContributionMap = this.getEdgeContributions(edges);
            Map<MyEdge, Long> edgeReachTimeMap = this.getEdgeReachTimes(edges);
            Map<MyEdge, Integer> edgeUniqueContributionMap = this.getEdgeUniqueContributions(edges);

            int nodeCnt = 0;
            for (MyEdge e : edges) {
                model.addRow(new String[]{
                    String.valueOf(++nodeCnt),
                    MySequentialGraphSysUtil.getNodeName(e.getSource().getName()),
                    MySequentialGraphSysUtil.getNodeName(e.getDest().getName()),
                    MyMathUtil.getCommaSeperatedNumber(edgeUniqueContributionMap.get(e)),
                    MyMathUtil.getCommaSeperatedNumber(edgeContributionMap.get(e)),
                    MyMathUtil.getCommaSeperatedNumber(edgeReachTimeMap.get(e)),
                    MyMathUtil.twoDecimalFormat(e.getSupport()),
                    MyMathUtil.twoDecimalFormat(e.getConfidence()),
                    MyMathUtil.twoDecimalFormat(e.getLift())});}

            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(250);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(150);
            this.table.getColumnModel().getColumn(4).setPreferredWidth(150);
            this.table.getColumnModel().getColumn(5).setPreferredWidth(150);
            this.table.getColumnModel().getColumn(6).setPreferredWidth(150);
            this.table.getColumnModel().getColumn(7).setPreferredWidth(150);
            this.table.getColumnModel().getColumn(8).setPreferredWidth(150);
            this.table.setBackground(Color.WHITE);
            this.table.setFont(MySequentialGraphVars.f_pln_12);
            this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    private void setEdgeStatistics() {
        try {
            this.model = new DefaultTableModel(this.data, this.columns);
            ArrayList<MyEdge> edges = new ArrayList<>(MySequentialGraphVars.g.getEdges());
            Collections.sort(edges);

            Map<MyEdge, Integer> edgeContributionMap = this.getEdgeContributions(edges);
            Map<MyEdge, Integer> edgeUniqueContributionMap = this.getEdgeUniqueContributions(edges);

            int nodeCnt = 0;
            for (MyEdge e : edges) {
                model.addRow(new String[]{
                    String.valueOf(++nodeCnt),
                    MySequentialGraphSysUtil.getNodeName(e.getSource().getName()),
                    MySequentialGraphSysUtil.getNodeName(e.getDest().getName()),
                    MyMathUtil.getCommaSeperatedNumber(edgeUniqueContributionMap.get(e)),
                    MyMathUtil.getCommaSeperatedNumber(edgeContributionMap.get(e)),
                    MyMathUtil.twoDecimalFormat(e.getSupport()),
                    MyMathUtil.twoDecimalFormat(e.getConfidence()),
                    MyMathUtil.twoDecimalFormat(e.getLift())});}

            this.table = new JTable(model);
            this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            this.table.getColumnModel().getColumn(0).setPreferredWidth(60);
            this.table.getColumnModel().getColumn(1).setPreferredWidth(200);
            this.table.getColumnModel().getColumn(2).setPreferredWidth(200);
            this.table.getColumnModel().getColumn(3).setPreferredWidth(100);
            this.table.getColumnModel().getColumn(4).setPreferredWidth(100);
            this.table.getColumnModel().getColumn(5).setPreferredWidth(100);
            this.table.getColumnModel().getColumn(6).setPreferredWidth(100);
            this.table.getColumnModel().getColumn(7).setPreferredWidth(100);
            this.table.setBackground(Color.WHITE);
            this.table.setFont(MySequentialGraphVars.f_pln_12);
            this.table.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
            this.table.setRowHeight(24);
        } catch (Exception ex) {}
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
