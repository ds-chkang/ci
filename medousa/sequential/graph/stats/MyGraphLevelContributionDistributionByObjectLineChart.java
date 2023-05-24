package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyComboBoxTooltipRenderer;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class MyGraphLevelContributionDistributionByObjectLineChart
extends JPanel {

    public MyGraphLevelContributionDistributionByObjectLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    BufferedReader br = new BufferedReader(new FileReader(new File(MySequentialGraphSysUtil.getWorkingDir() +
                        MySequentialGraphSysUtil.getDirectorySlash() + "sequenceWithObjectIDs.txt")));
                    Map<String, Integer> co;
                    String line = "";
                    while ((line = br.readLine()) != null) {
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries initialNodeCountSeries = new XYSeries("O. N. CNT.");
                    TreeMap<Integer, Integer> openNodeCountMap = new TreeMap<>();
                    Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                    for (MyNode n : nodes) {
                        if (n.getCurrentValue() <= 0) continue;
                        int value = n.getStartPositionNodeCount();
                        if (openNodeCountMap.containsKey(value)) {
                            openNodeCountMap.put(value, openNodeCountMap.get(value) + 1);
                        } else {
                            openNodeCountMap.put(value, 1);
                        }
                    }

                    JFreeChart chart = ChartFactory.createXYLineChart("", "CONTRIBUTION COUNT BY OBJECT", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.DARK_GRAY);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" CONT. BY OBJ.");
                    titleLabel.setToolTipText("CONTRIBUTION COUNT BY OBJECT DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarge();
                                }
                            }).start();
                        }
                    });

                    JPanel enlargePanel = new JPanel();
                    enlargePanel.setBackground(Color.WHITE);
                    enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    enlargePanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(enlargePanel, BorderLayout.CENTER);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });

    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" NODE COUNT DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyGraphLevelContributionDistributionByObjectLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.setCursor(Cursor.HAND_CURSOR);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setAlwaysOnTop(true);
            f.pack();
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }
}
