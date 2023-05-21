package medousa.direct.graph.multilevel;

import medousa.MyProgressBar;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart extends JPanel {

    private boolean MAXIMIZED;

    public MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart() {
        this.decorate();
    }
    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3,3));
                    setBackground(Color.WHITE);

                    ChartPanel chartPanel = new ChartPanel(setChart());
                    chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
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

                    JLabel titleLabel = new JLabel(" S. V.");
                    titleLabel.setToolTipText("SUCCESSOR VALUE DISTRIBUTION");
                    titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setToolTipText("ENLARGE");
                    enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont11);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override
                        public void run() {
                            enlarge();
                        }
                        }).start();
                    }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0,0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private JFreeChart setChart() {
        try {
            int totalCnt = 0;
            double totalValue = 0;
            Map<Integer, Integer> valueMap = new HashMap<>();
            if (MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet != null) {
                for (MyDirectNode n : MyDirectGraphVars.getDirectGraphViewer().multiNodeSuccessorSet) {
                    if (n.getCurrentValue() > 0) {
                        int value = (int) n.getCurrentValue();
                        totalCnt++;
                        totalValue += n.getCurrentValue();
                        if (valueMap.containsKey(value)) {
                            valueMap.put(value, valueMap.get(value) + 1);
                        } else {
                            valueMap.put(value, 1);
                        }
                    }
                }
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Integer value : valueMap.keySet()) {
                dataset.addValue(valueMap.get(value), "S. V.", value);
            }

            if (totalCnt == 0) {
                totalCnt = 1;
            }

            String avgValue = MyDirectGraphMathUtil.twoDecimalFormat((double) totalValue/totalCnt);
            String plotTitle = "";
            String xAxis = "[AVG.: " + MyDirectGraphSysUtil.formatAverageValue(avgValue) + "]";
            String yAxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            return ChartFactory.createBarChart(plotTitle, xAxis, yAxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {ex.printStackTrace();}
        return null;
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart multiNodeLevelSuccessorValueDistributionLineChart = new MyDirectGraphMultiNodeLevelSuccessorValueDistributionLineChart();
            multiNodeLevelSuccessorValueDistributionLineChart.MAXIMIZED = true;
            JFrame f = new JFrame(" PREDECESSOR VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            f.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 300));
            f.getContentPane().add(multiNodeLevelSuccessorValueDistributionLineChart, BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

}
