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
    private float toTime = 1f;
    private int selectedTime = 0;
    private int selectedGraph = 0;
    private XYSeries totalReachTimeByDepthSeries;
    private Map<Integer, Long> totalReachTimeByDepthMap;
    private XYSeries maxReachTimeByDepthSeries;
    private Map<Integer, Long> maxReachTimeByDepthMap;
    private XYSeries minReachTimeByDepthSeries;
    private Map<Integer, Long> minReachTimeByDepthMap = new HashMap<>();

    public MySingleNodeReachTimeByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
        this.totalReachTimeByDepthSeries = new XYSeries("TOTAL");
        this.totalReachTimeByDepthMap = new HashMap<>();
        this.maxReachTimeByDepthSeries = new XYSeries("MAX");
        this.maxReachTimeByDepthMap = new HashMap<>();
        this.minReachTimeByDepthSeries = new XYSeries("MIN.");
        this.minReachTimeByDepthMap = new HashMap<>();

        MyNode selectedNode = null;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
            selectedNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
        } else {
            selectedNode = MySequentialGraphVars.getSequentialGraphViewer().singleNode;
        }

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            this.minReachTimeByDepthMap.put(i, 10000000000000000L);
        }

        this.totalReachTimeByDepthMap.put(1, 0L);
        this.totalReachTimeByDepthSeries.add(1, 0);

        this.maxReachTimeByDepthMap.put(1, 0L);
        this.maxReachTimeByDepthSeries.add(1, 0);

        this.minReachTimeByDepthMap.put(1, 0L);
        this.minReachTimeByDepthSeries.add(1, 0);

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            if (selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                totalReachTimeByDepthMap.put(i, (long) ((float)selectedNode.getNodeDepthInfo(i).getReachTime()/toTime));
            } else {
                totalReachTimeByDepthMap.put(i, 0L);
            }
        }

        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length; i++) {
                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (n.equals(selectedNode.getName())) {
                    long time = (long) (Float.parseFloat(MySequentialGraphVars.seqs[s][i].split(":")[1])/toTime);
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

        for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
            for (int i = 1; i < MySequentialGraphVars.seqs[s].length - 1; i++) {
                String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                if (n.equals(selectedNode.getName())) {
                    long time = (long) (Float.parseFloat(MySequentialGraphVars.seqs[s][i].split(":")[1])/toTime);
                    if (time > 0) {
                        if (this.minReachTimeByDepthMap.containsKey(i+1)) {
                            if (this.minReachTimeByDepthMap.get(i+1) > time) {
                                this.minReachTimeByDepthMap.put(i+1, time);
                            }
                        } else {
                            this.minReachTimeByDepthMap.put(i+1, time);
                        }
                    }
                }
            }
        }

        for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.maxReachTimeByDepthMap.containsKey(i)) {
                this.maxReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.maxReachTimeByDepthSeries.add(i, (this.maxReachTimeByDepthMap.get(i)));
            }
        }

        for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.totalReachTimeByDepthMap.containsKey(i)) {
                this.totalReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.totalReachTimeByDepthSeries.add(i, (this.totalReachTimeByDepthMap.get(i)));
            }
        }

        for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
            if (!this.minReachTimeByDepthMap.containsKey(i)) {
                this.minReachTimeByDepthSeries.add(i, 0D);
            } else if (this.minReachTimeByDepthMap.get(i) == 10000000000000000L) {
                this.minReachTimeByDepthSeries.add(i, 0D);
            } else {
                this.minReachTimeByDepthSeries.add(i, (this.minReachTimeByDepthMap.get(i)));
            }
        }
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(2,2));
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

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setFocusable(false);
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("TOTAL");
                    graphMenu.addItem("MAX.");
                    graphMenu.addItem("MIN.");
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();
                        }
                    });

                    JComboBox timeMenu = new JComboBox();
                    timeMenu.addItem("SECOND");
                    timeMenu.addItem("MINUTE");
                    timeMenu.addItem("HOUR");
                    timeMenu.setSelectedIndex(selectedTime);
                    timeMenu.setFont(MySequentialGraphVars.tahomaPlainFont12);
                    timeMenu.setFocusable(false);
                    timeMenu.setBackground(Color.WHITE);
                    timeMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    if (timeMenu.getSelectedIndex() == 0) {
                                        toTime = 1;
                                        selectedTime = 0;
                                        decorate();
                                    } else if (timeMenu.getSelectedIndex() == 1) {
                                        selectedTime = 1;
                                        toTime = 60;
                                        decorate();
                                    } else if (timeMenu.getSelectedIndex() == 2) {
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
                    btnPanel.add(timeMenu);
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
