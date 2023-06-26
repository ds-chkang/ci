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

public class MyDirectGraphTopLevelNodeLabelDistribution
extends JPanel
implements ActionListener {

    protected boolean MAXIMIZED;


    public MyDirectGraphTopLevelNodeLabelDistribution() {
        this.decorate();
    }

    public void decorate() {
        final MyDirectGraphTopLevelNodeLabelDistribution nodeValueLabelDistribution = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3, 3));
                setBackground(Color.WHITE);

                if (MyDirectGraphVars.getDirectGraphViewer().vc.nodeLabelComboBoxMenu.getSelectedIndex() < 2) {

                    JLabel titleLabel = new JLabel(" N. L.");
                    titleLabel.setToolTipText("NODE LABEL DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 6));
                    titlePanel.add(titleLabel);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 8));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);

                    JLabel msg = new JLabel("N/A");
                    msg.setFont(MyDirectGraphVars.tahomaPlainFont12);
                    msg.setBackground(Color.WHITE);
                    msg.setHorizontalAlignment(JLabel.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    add(msg, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                    return;
                }

                ChartPanel chartPanel = new ChartPanel(setLabelChart());
                chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont10);
                chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.tahomaPlainFont10);

                CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
                domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

                BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
                barRenderer.setSeriesPaint(0, new Color(0,0,0,0.3f));
                barRenderer.setShadowPaint(Color.WHITE);
                barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
                barRenderer.setBarPainter(new StandardBarPainter());
                barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont12);

                JLabel titleLabel = new JLabel(" N. L.");
                titleLabel.setToolTipText("NODE LABEL DISTRIBUTION");
                titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setToolTipText("ENLARGE");
                enlargeBtn.setFocusable(false);
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        enlarge();
                                    }
                                }).start();
                            }
                        });
                    }
                });

                JPanel menuPanel = new JPanel();
                menuPanel.setBackground(Color.WHITE);
                menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                menuPanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3, 3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(menuPanel, BorderLayout.EAST);

                add(chartPanel, BorderLayout.CENTER);
                add(topPanel, BorderLayout.NORTH);


                chartPanel.getChart().removeLegend();
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
            MyProgressBar pb = new MyProgressBar(false);
            try {
                MyDirectGraphTopLevelNodeLabelDistribution nodeValueLabelDistribution = new MyDirectGraphTopLevelNodeLabelDistribution();
                nodeValueLabelDistribution.MAXIMIZED = true;

                String[] nodeTableColumns = {"NO.", "NODE", "L.", "C."};
                String[][] nodeTableData = {};
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

                int i = 0;
                Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
                LinkedHashMap<String, Long> valueMap = new LinkedHashMap();
                for (MyDirectNode n : nodes) {
                    valueMap.put(n.getName(), (long) MyDirectGraphVars.directGraph.getSuccessorCount(n));
                }
                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);

                for (String n : valueMap.keySet()) {
                    String pr = MyDirectGraphMathUtil.twoDecimalFormat((double) valueMap.get(n) / MyDirectGraphVars.directGraph.maxNodeValue);
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
                JPanel nodeTableSearchPanel = MyTableUtil.searchAndSaveDataPanelForJTable2(nodeValueLabelDistribution, nodeSearchTxt, nodeSelectBtn, nodeTableModel, nodeTable);
                nodeSearchTxt.setFont(MyDirectGraphVars.f_bold_12);
                nodeSearchTxt.setToolTipText("TYPE A NODE NAME TO SEARCH");
                nodeSearchTxt.setPreferredSize(new Dimension(90, 19));
                nodeSelectBtn.setPreferredSize(new Dimension(54, 19));
                nodeTableSearchPanel.remove(nodeSelectBtn);

                JPanel nodeTablePanel = new JPanel();
                nodeTablePanel.setBackground(Color.WHITE);
                nodeTablePanel.setLayout(new BorderLayout(3, 3));
                nodeTablePanel.add(nodeTableScrollPane, BorderLayout.CENTER);
                nodeTablePanel.add(nodeTableSearchPanel, BorderLayout.SOUTH);

                JFrame f = new JFrame(" NODE LABEL DISTRIBUTION");
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

                JSplitPane contentPane = new JSplitPane();
                contentPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentPane.setLeftComponent(nodeValueLabelDistribution);
                contentPane.setRightComponent(new JScrollPane(nodeTablePanel));
                contentPane.getRightComponent().setBackground(Color.WHITE);
                contentPane.setDividerSize(5);

                f.setLayout(new BorderLayout(3, 3));
                f.getContentPane().add(contentPane, BorderLayout.CENTER);
                f.pack();
                f.addComponentListener(new ComponentAdapter() {
                    @Override public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        contentPane.setDividerLocation(0.86);
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

    private JFreeChart setLabelChart() {
        TreeMap<String, Integer> labelMap = new TreeMap<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (labelMap.containsKey(n.getCurrentLabel())) {
                labelMap.put(n.getCurrentLabel(), labelMap.get(n.getCurrentLabel()) + 1);
            } else {
                labelMap.put(n.getCurrentLabel(), 1);
            }


        }


        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (String nodeLabel : labelMap.keySet()) {
            dataset.addValue(labelMap.get(nodeLabel), "", nodeLabel);
        }

        String plotTitle = "";
        String xaxis = "NODE LABEL DISTRIBUTION";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }

    @Override public void actionPerformed(ActionEvent e) {

    }
}
