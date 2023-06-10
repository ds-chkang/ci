package medousa.direct.graph.toplevel;

import medousa.direct.graph.MyDirectNode;
import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class MyDirectGraphTopLevelSuccessorCountDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;
    private int maxSuccessor = 0;
    private int minSuccessor = 1000000000;
    private double avgSuccessor = 0;
    private double stdSuccessor = 0;
    public MyDirectGraphTopLevelSuccessorCountDistribution() {
        this.decorate();
    }
    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setChart());
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont12);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
                    chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont11);

                    CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                    domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                    BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                    barRenderer.setSeriesPaint(0, new Color(0, 0, 0, 0.25f));//Color.LIGHT_GRAY);//Color.decode("#2084FE"));
                    barRenderer.setShadowPaint(Color.WHITE);
                    barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                    barRenderer.setBarPainter(new StandardBarPainter());
                    barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
                    JLabel titleLabel = new JLabel(" S. C.");
                    titleLabel.setToolTipText("SUCCESSOR COUNT DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setToolTipText("ENLARGE");
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override public void run() {
                                    new Thread(new Runnable() {
                                        @Override public void run() {
                                            enlarge();
                                        }
                                    }).start();
                                }
                            });
                        }
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    buttonPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setBackground(Color.WHITE);
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(buttonPanel, BorderLayout.EAST);

                    add(chartPanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);

                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphTopLevelSuccessorCountDistribution topLevelSuccessorCountDistribution = new MyDirectGraphTopLevelSuccessorCountDistribution();
            topLevelSuccessorCountDistribution.MAXIMIZED = true;

            String [] statTableColumns = {"PROPERTY", "VALUE"};
            String [][] statTableData = {
                    {"MAX. SUCCESSOR", MyDirectGraphMathUtil.getCommaSeperatedNumber(maxSuccessor)},
                    {"NIN. SUCCESSOR", MyDirectGraphMathUtil.getCommaSeperatedNumber(minSuccessor)},
                    {"AVG. SUCCESSOR", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(avgSuccessor))},
                    {"STD. SUCCESSOR", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(stdSuccessor))}
            };

            DefaultTableModel statTableModel = new DefaultTableModel(statTableData, statTableColumns);
            JTable statTable = new JTable(statTableModel);
            statTable.setBackground(Color.WHITE);
            statTable.setFont(MyDirectGraphVars.tahomaPlainFont12);
            statTable.setRowHeight(22);
            statTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
            statTable.getTableHeader().setOpaque(false);
            statTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            statTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(120);

            JScrollPane statTableScrollPane = new JScrollPane(statTable);
            statTableScrollPane.setBackground(Color.WHITE);

            String [] nodeTableColumns = {"NO.", "NODE", "S.", "S. R."};
            String [][] nodeTableData = {};
            DefaultTableModel nodeTableModel = new DefaultTableModel(nodeTableData, nodeTableColumns);
            JTable nodeTable = new JTable(nodeTableModel);
            nodeTable.setBackground(Color.WHITE);
            nodeTable.setFont(MyDirectGraphVars.f_pln_12);
            nodeTable.setRowHeight(22);
            nodeTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont11);
            nodeTable.getTableHeader().setOpaque(false);
            nodeTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            nodeTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(25);
            nodeTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(25);
            nodeTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(25);

            int i=0;
            Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
            LinkedHashMap<String, Long> valueMap = new LinkedHashMap();
            for (MyDirectNode n : nodes) {
                valueMap.put(n.getName(), (long) MyDirectGraphVars.directGraph.getSuccessorCount(n));
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);

            for (String n : valueMap.keySet()) {
                String pr = MyDirectGraphMathUtil.twoDecimalFormat((double) valueMap.get(n)/ MyDirectGraphVars.directGraph.getVertexCount());
                nodeTableModel.addRow(new String[]{"" + (++i), n, MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(n)), pr});
            }

            JScrollPane nodeTableScrollPane = new JScrollPane(nodeTable);
            nodeTableScrollPane.setBackground(Color.WHITE);

            JTextField nodeSearchTxt = new JTextField();
            JButton nodeSelectBtn = new JButton("SEL.");
            nodeSelectBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            nodeSelectBtn.setFocusable(false);
            nodeSearchTxt.setBackground(Color.WHITE);
            nodeSearchTxt.setBorder(BorderFactory.createLoweredSoftBevelBorder());
            JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(topLevelSuccessorCountDistribution, nodeSearchTxt, nodeSelectBtn, nodeTableModel, nodeTable);
            nodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
            nodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
            nodeSearchTxt.setPreferredSize(new Dimension(90, 19));
            nodeSelectBtn.setPreferredSize(new Dimension(54, 19));
            nodeTableSearchPanel.remove(nodeSelectBtn);

            JPanel nodeTablePanel = new JPanel();
            nodeTablePanel.setBackground(Color.WHITE);
            nodeTablePanel.setLayout(new BorderLayout(3,3));
            nodeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
            nodeTablePanel.add(nodeTableSearchPanel, BorderLayout.SOUTH);

            JFrame f = new JFrame(" SUCCESSOR COUNT DISTRIBUTION");
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(550, 450));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setTopComponent(statTableScrollPane);
            tableSplitPane.setBottomComponent(nodeTablePanel);
            tableSplitPane.getRightComponent().setBackground(Color.WHITE);
            tableSplitPane.setDividerSize(5);

            JSplitPane contentPane = new JSplitPane();
            contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            contentPane.setLeftComponent(topLevelSuccessorCountDistribution);
            contentPane.setRightComponent(tableSplitPane);
            contentPane.getRightComponent().setBackground(Color.WHITE);
            contentPane.setDividerSize(5);

            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(contentPane, BorderLayout.CENTER);
            f.pack();
            f.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    contentPane.setDividerLocation(0.86);
                    tableSplitPane.setDividerLocation(0.20);
                }
            });

            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();

            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    private JFreeChart setChart() {
        int nodeCount = 0;
        int totalSuccessors = 0;

        TreeMap<Integer, Integer> valueMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            int value = MyDirectGraphVars.directGraph.getSuccessorCount(n);
            totalSuccessors += value;
            nodeCount++;

            if (value > this.maxSuccessor) {
                this.maxSuccessor = value;
            }

            if (value > 0 && value < this.minSuccessor) {
                this.minSuccessor = value;
            }

            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1);
            }
        }

        this.avgSuccessor = (double) totalSuccessors/nodeCount;
        this.stdSuccessor = getSuccessorCountStandardDeviation(valueMap);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer avgSuccessorCount : valueMap.keySet()) {
            dataset.addValue(valueMap.get(avgSuccessorCount), "S. C.", avgSuccessorCount);
        }

        String plotTitle = "";
        String xaxis = "SUCCESSOR COUNT DISTRIBUTION";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }


    public double getSuccessorCountStandardDeviation(TreeMap<Integer, Integer> valueMap) {
        double sum = 0.00d;
        for (int n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0D;
        for (int n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00D ? 0.00D : Math.sqrt(sum / valueMap.size()));
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
