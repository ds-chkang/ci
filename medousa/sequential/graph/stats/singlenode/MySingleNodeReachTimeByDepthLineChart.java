package medousa.sequential.graph.stats.singlenode;

import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
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
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

public class MySingleNodeReachTimeByDepthLineChart
extends JPanel {

    public static int instances = 0;
    JComboBox graphSelectionOption;

    public MySingleNodeReachTimeByDepthLineChart() {
        this.decorate();
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    MyNode selectedSingleNode = null;
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                        selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
                    } else {
                        selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().singleNode;
                    }

                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    XYSeries totalReachTimeByDepthSeries = new XYSeries("TOTAL");
                    Map<Integer, Long> totalReachTimeByDepthMap = new HashMap<>();

                    totalReachTimeByDepthMap.put(1, 0L);
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                            totalReachTimeByDepthMap.put(i, selectedSingleNode.getNodeDepthInfo(i).getReachTime());
                        } else {
                            totalReachTimeByDepthMap.put(i, 0L);
                        }
                    }

                    totalReachTimeByDepthSeries.add(1, 0);
                    for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (!totalReachTimeByDepthMap.containsKey(i)) {
                            totalReachTimeByDepthSeries.add(i, 0D);
                        } else {
                            totalReachTimeByDepthSeries.add(i, (double) (totalReachTimeByDepthMap.get(i)));
                        }
                    }

                    XYSeries maxInReachTimeByDepthSeries = new XYSeries("MAX. IN");
                    Map<Integer, Long> maxInReachTimeByDepthMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (n.equals(selectedSingleNode.getName())) {
                                long time = Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1]);
                                if (maxInReachTimeByDepthMap.containsKey(i + 1)) {
                                    if (maxInReachTimeByDepthMap.get(i + 1) < time) {
                                        maxInReachTimeByDepthMap.put(i + 1, time);
                                    }
                                } else {
                                    maxInReachTimeByDepthMap.put(i + 1, time);
                                }
                            }
                        }
                    }

                    maxInReachTimeByDepthSeries.add(1, 0);
                    for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (!maxInReachTimeByDepthMap.containsKey(i)) {
                            maxInReachTimeByDepthSeries.add(i, 0D);
                        } else {
                            maxInReachTimeByDepthSeries.add(i, (double)(maxInReachTimeByDepthMap.get(i)));
                        }
                    }

                    XYSeries maxOutReachTimeByDepthSeries = new XYSeries("MAX. OUT");
                    Map<Integer, Long> maxOutReachTimeByDepthMap = new HashMap<>();
                    for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                        for (int i = 0; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                            String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                            if (n.equals(selectedSingleNode.getName())) {
                                long time = Long.parseLong(MySequentialGraphVars.seqs[s][i + 1].split(":")[1]);
                                if (maxOutReachTimeByDepthMap.containsKey(i + 1)) {
                                    if (maxOutReachTimeByDepthMap.get(i + 1) < time) {
                                        maxOutReachTimeByDepthMap.put(i + 1, time);
                                    }
                                } else {
                                    maxOutReachTimeByDepthMap.put(i + 1, time);
                                }
                            }
                        }
                    }

                    maxOutReachTimeByDepthSeries.add(1, 0);
                    for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (!maxOutReachTimeByDepthMap.containsKey(i)) {
                            maxOutReachTimeByDepthSeries.add(i, 0D);
                        } else {
                            maxOutReachTimeByDepthSeries.add(i, (double)(maxOutReachTimeByDepthMap.get(i)));
                        }
                    }

                    Map<Integer, Integer> inContributionMap = new HashMap<>();
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                            inContributionMap.put(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution());
                        } else {
                            inContributionMap.put(i, 0);
                        }
                    }



                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(totalReachTimeByDepthSeries);
                    dataset.addSeries(maxInReachTimeByDepthSeries);
                    dataset.addSeries(maxOutReachTimeByDepthSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.RED);
                    renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(0, true);
                    renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(0, Color.WHITE);

                    renderer.setSeriesPaint(1, Color.BLUE);
                    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(1, true);
                    renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(1, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(2, Color.DARK_GRAY);
                    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(2, true);
                    renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(2, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    ChartPanel chartPanel = new ChartPanel( chart );

                    JLabel titleLabel = new JLabel(" REACH TIME");
                    titleLabel.setToolTipText("REACH TIME BY DEPTH");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {@Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {@Override public void run() {
                            enlarge();
                        }}).start();
                    }
                    });

                    graphSelectionOption = new JComboBox();
                    graphSelectionOption.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphSelectionOption.setFocusable(false);
                    graphSelectionOption.setBackground(Color.WHITE);
                    graphSelectionOption.addItem("SELECT");
                    graphSelectionOption.addItem("TOTAL");
                    graphSelectionOption.addItem("MAX. IN");
                    graphSelectionOption.addItem("MAX. OUT");
                    graphSelectionOption.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            graphSelectionOption.setSelectedIndex(graphSelectionOption.getSelectedIndex());
                            setLineGraphRendering(renderer, graphSelectionOption.getSelectedIndex());
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphSelectionOption);
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0,0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    revalidate();
                    repaint();
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    private void setLineGraphRendering(XYLineAndShapeRenderer renderer, int selected) {
        Color colors [] = {Color.RED, Color.BLUE, Color.DARK_GRAY, Color.ORANGE};
        if (selected == 0) {
            renderer.setSeriesPaint(0, colors[0]);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, colors[1]);
            renderer.setSeriesStroke(1, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);

            renderer.setSeriesPaint(2, colors[2]);
            renderer.setSeriesStroke(2, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(2, true);
            renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(2, Color.WHITE);

        } else {
            for (int i = 0; i < 3; i++) {
                if (i == (selected-1)) {
                    renderer.setSeriesPaint(i, colors[i]);
                    renderer.setSeriesStroke(i, new BasicStroke(1.5f));
                    renderer.setSeriesLinesVisible(i, true);
                    renderer.setSeriesFillPaint(i, Color.WHITE);
                    renderer.setSeriesShape(i, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                } else {
                    renderer.setSeriesPaint(i, new Color(0f, 0f, 0f, 0f));
                    renderer.setSeriesStroke(i, new BasicStroke(0f));
                    renderer.setSeriesLinesVisible(i, false);
                    renderer.setSeriesFillPaint(i, new Color(0f, 0f, 0f, 0f));
                    renderer.setSeriesShape(i, new Ellipse2D.Double(0f, 0f, 0f, 0f));
                }
            }
        }

        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("REACH TIME BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 450));
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.getContentPane().add(new MySingleNodeReachTimeByDepthLineChart(), BorderLayout.CENTER);
            f.pack();

            Border blackline = BorderFactory.createLineBorder(Color.black);
            TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
            titledBorder.setTitleJustification(TitledBorder.LEFT);
            titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
            titledBorder.setTitleColor(Color.DARK_GRAY);

            f.setCursor(Cursor.HAND_CURSOR);
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

}
