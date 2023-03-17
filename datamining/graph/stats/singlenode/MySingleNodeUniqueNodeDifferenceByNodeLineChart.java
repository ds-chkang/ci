package datamining.graph.stats.singlenode;

import datamining.graph.MyNode;
import datamining.main.MyProgressBar;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class MySingleNodeUniqueNodeDifferenceByNodeLineChart
extends JPanel {

    public static int instances = 0;
    public MySingleNodeUniqueNodeDifferenceByNodeLineChart() {
        this.decorate();
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(5, 5));
                    setBackground(Color.WHITE);

                    MyNode selectedSingleNode = null;
                    if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                        selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                    } else {
                        selectedSingleNode = MyVars.getViewer().selectedNode;
                    }

                    Map<Integer, Integer> inOutNodeDifferenceValueMap = new HashMap<>();
                    XYSeries inOutNodeDifferenceSeries = new XYSeries("UNIQ. NODE DIFF.");
                    for (int i = 1; i <= MyVars.mxDepth; i++) {
                        if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                            int diff = (selectedSingleNode.getNodeDepthInfo(i).getPredecessorCount() -
                                    selectedSingleNode.getNodeDepthInfo(i).getSuccessorCount());
                            inOutNodeDifferenceValueMap.put(i, diff);
                        } else {
                            inOutNodeDifferenceValueMap.put(i, 0);
                        }
                    }

                    for (int i = 1; i < MyVars.mxDepth; i++) {
                        inOutNodeDifferenceSeries.add(i, (double) inOutNodeDifferenceValueMap.get(i));
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(inOutNodeDifferenceSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "", "", dataset);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getTitle().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().getDomainAxis().setAutoRange(true);
                    chart.getXYPlot().getRangeAxis().setAutoRange(true);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.MAGENTA);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesFillPaint(0, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    chart.getXYPlot().setOutlineVisible(true);
                    chart.getXYPlot().getDomainAxis().setMinorTickCount(1);
                    NumberAxis domain = (NumberAxis)chart.getXYPlot().getDomainAxis();
                    domain.setVerticalTickLabels(true);

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.getChart().getPlot().setBackgroundPaint(Color.WHITE);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                    JLabel titleLabel = new JLabel("UNIQUE NODE DIFF.");
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setHorizontalAlignment(JLabel.LEFT);
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override public void run() {
                            enlarge();
                        }}).start();}});

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new GridLayout(1,2));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);

                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void enlarge() {
        try {
            MyProgressBar pb = new MyProgressBar(false);
            JFrame frame = new JFrame("UNIQUE NODE DIFFERENCE BY DEPTH");
            frame.setLayout(new BorderLayout());
            frame.setBackground(Color.WHITE);
            frame.setPreferredSize(new Dimension(450, 400));
            pb.updateValue(20, 100);

            MySingleNodeUniqueNodeDifferenceByNodeLineChart inOutNodeContribtuionDifferenceDistributionChart = new MySingleNodeUniqueNodeDifferenceByNodeLineChart();
            frame.getContentPane().add(inOutNodeContribtuionDifferenceDistributionChart, BorderLayout.CENTER);
            frame.pack();
            pb.updateValue(60, 100);

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "UNIQUE NODE DIFFERENCE BY DEPTH");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(MyVars.tahomaBoldFont11);
            inOutNodeContribtuionDifferenceDistributionChart.setBorder(titledBorder);
            frame.setCursor(Cursor.HAND_CURSOR);
            pb.updateValue(100, 100);
            pb.dispose();

            frame.setVisible(true);

            frame.setAlwaysOnTop(true);
            frame.setAlwaysOnTop(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
