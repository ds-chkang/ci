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
    private int selectedGraph;
    private float toTime = 1f;
    private int selectedTime = 0;

    private Map<Integer, Long> totalReachTimeByDepthMap;
    private Map<Integer, Long> maxReachTimeByDepthMap;
    private Map<Integer, Long> minReachTimeByDepthMap;

    private XYSeries maxReachTimeByDepthSeries;
    private XYSeries totalReachTimeByDepthSeries;
    private XYSeries minReachTimeByDepthSeries;

    public MySingleNodeReachTimeByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
        MyNode selectedSingleNode = null;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
            selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
        } else {
            selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().singleNode;
        }

        this.totalReachTimeByDepthSeries = new XYSeries("TOTAL");
        this.totalReachTimeByDepthMap = new HashMap<>();
        this.minReachTimeByDepthSeries = new XYSeries("MIN.");
        this.minReachTimeByDepthMap = new HashMap<>();
        this.maxReachTimeByDepthSeries = new XYSeries("MAX.");
        this.maxReachTimeByDepthMap = new HashMap<>();

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            minReachTimeByDepthMap.put(i, 10000000000000000L);
        }

        this.totalReachTimeByDepthMap.put(1, 0L);
        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                this.totalReachTimeByDepthMap.put(i, (long) (selectedSingleNode.getNodeDepthInfo(i).getReachTime()/toTime));
            } else {
                this.totalReachTimeByDepthMap.put(i, 0L);
            }
        }

        this.totalReachTimeByDepthSeries.add(1, 0);
        for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.totalReachTimeByDepthMap.containsKey(i)) {
                this.totalReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.totalReachTimeByDepthSeries.add(i, this.totalReachTimeByDepthMap.get(i));
            }
        }

        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (n.equals(selectedSingleNode.getName())) {
                    long time = (long) (Long.parseLong(MySequentialGraphVars.seqs[s][i].split(":")[1])/toTime);
                    if (this.maxReachTimeByDepthMap.containsKey(i+1)) {
                        if (this.maxReachTimeByDepthMap.get(i+1) < time) {
                            this.maxReachTimeByDepthMap.put(i+1, time);
                        }
                    } else {
                        this.maxReachTimeByDepthMap.put(i+1, time);
                    }
                }
            }
        }

        this.maxReachTimeByDepthSeries.add(1, 0);
        for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.maxReachTimeByDepthMap.containsKey(i)) {
                this.maxReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.maxReachTimeByDepthSeries.add(i, this.maxReachTimeByDepthMap.get(i));
            }
        }

        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 0; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (n.equals(selectedSingleNode.getName())) {
                    long time = (long) (Long.parseLong(MySequentialGraphVars.seqs[s][i + 1].split(":")[1])/toTime);
                    if (time > 0) {
                        if (this.minReachTimeByDepthMap.containsKey(i+1)) {
                            if (this.minReachTimeByDepthMap.get(i+1) > time) {
                                this.minReachTimeByDepthMap.put(i+1, time);
                            }
                        } else {
                            minReachTimeByDepthMap.put(i+1, time);
                        }
                    }
                }
            }
        }

        this.minReachTimeByDepthSeries.add(1, 0);
        for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.minReachTimeByDepthMap.containsKey(i)) {
                this.minReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.minReachTimeByDepthSeries.add(i, this.minReachTimeByDepthMap.get(i));
            }
        }
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3,3));
                    setBackground(Color.WHITE);
                    setData();

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (selectedGraph == 0) {
                        dataset.addSeries(totalReachTimeByDepthSeries);
                        dataset.addSeries(maxReachTimeByDepthSeries);
                        dataset.addSeries(minReachTimeByDepthSeries);
                    } else if (selectedGraph == 1) {
                        dataset.addSeries(totalReachTimeByDepthSeries);
                    } else if (selectedGraph == 2) {
                        dataset.addSeries(maxReachTimeByDepthSeries);
                    } else if (selectedGraph == 3) {
                        dataset.addSeries(minReachTimeByDepthSeries);
                    }

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

                    JLabel titleLabel = new JLabel(" R. T.");
                    titleLabel.setToolTipText("REACH TIME BY DEPTH FOR SELECTED NODE");
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
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                        }}).start();
                    }
                    });

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("TOTAL");
                    graphMenu.addItem("MAX.");
                    graphMenu.addItem("MIN.");
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setFocusable(false);
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            setLineGraphRendering(renderer, selectedGraph);
                        }
                    });

                    JComboBox timeConvertMenu = new JComboBox();
                    timeConvertMenu.addItem("SECOND");
                    timeConvertMenu.addItem("MINUTE");
                    timeConvertMenu.addItem("HOUR");
                    timeConvertMenu.setSelectedIndex(selectedTime);
                    timeConvertMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
                    timeConvertMenu.setFocusable(false);
                    timeConvertMenu.setBackground(Color.WHITE);
                    timeConvertMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (timeConvertMenu.getSelectedIndex() == 0) {
                                        toTime = 1;
                                        selectedTime = 0;
                                        decorate();
                                    } else if (timeConvertMenu.getSelectedIndex() == 1) {
                                        toTime = 60;
                                        selectedTime = 1;
                                        decorate();
                                    } else if (timeConvertMenu.getSelectedIndex() == 2) {
                                        toTime = 3600;
                                        selectedTime = 2;
                                        decorate();
                                    }
                                }
                            }).start();
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphMenu);
                    btnPanel.add(timeConvertMenu);
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
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
        } catch (Exception ex) {
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

}
