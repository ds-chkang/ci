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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public class MySingleNodeContributionByDepthLineChart
extends JPanel {

    public static int instances = 0;
    private int selectedGraph;
    private XYSeries contributionByDepthSeries;
    private XYSeries inContributionByDepthSeries;
    private XYSeries outContributionByDepthSeries;
    private XYSeries inOutDiffContributionByDepthSeries;

    public MySingleNodeContributionByDepthLineChart() {
        this.decorate();
    }

    private void setData() {
        this.contributionByDepthSeries = new XYSeries("CONT.");
        this.inContributionByDepthSeries = new XYSeries("IN");
        this.outContributionByDepthSeries = new XYSeries("OUT");
        this.inOutDiffContributionByDepthSeries = new XYSeries("DIFF.");

        MyNode selectedSingleNode = null;
        if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
            selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
        } else {
            selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().singleNode;
        }

        if (MySequentialGraphVars.currentGraphDepth > 0) {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (MySequentialGraphVars.currentGraphDepth == i) {
                    if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                        this.contributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getContribution());
                        this.inContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution());
                        this.outContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                        this.inOutDiffContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution()-selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                    } else {
                        this.contributionByDepthSeries.add(i, 0);
                        this.inContributionByDepthSeries.add(i, 0);
                        this.outContributionByDepthSeries.add(i, 0);
                        this.inOutDiffContributionByDepthSeries.add(i, 0);
                    }
                } else {
                    this.contributionByDepthSeries.add(i, 0);
                    this.inContributionByDepthSeries.add(i, 0);
                    this.outContributionByDepthSeries.add(i, 0);
                    this.inOutDiffContributionByDepthSeries.add(i, 0);
                }
            }
        } else {
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                    this.contributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getContribution());
                    this.inContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution());
                    this.outContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                    this.inOutDiffContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution()-selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                } else {
                    this.contributionByDepthSeries.add(i, 0);
                    this.inContributionByDepthSeries.add(i, 0);
                    this.outContributionByDepthSeries.add(i, 0);
                    this.inOutDiffContributionByDepthSeries.add(i, 0);
                }
            }
        }
    }

    private void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);
                    setData();

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    if (selectedGraph == 0) {
                        dataset.addSeries(contributionByDepthSeries);
                        dataset.addSeries(inContributionByDepthSeries);
                        dataset.addSeries(outContributionByDepthSeries);
                        dataset.addSeries(inOutDiffContributionByDepthSeries);
                    } else if (selectedGraph == 1) {
                        dataset.addSeries(contributionByDepthSeries);
                    } else if (selectedGraph == 2) {
                        dataset.addSeries(inContributionByDepthSeries);
                    } else if (selectedGraph == 3) {
                        dataset.addSeries(outContributionByDepthSeries);
                    } else if (selectedGraph == 4) {
                        dataset.addSeries(inOutDiffContributionByDepthSeries);
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

                    renderer.setSeriesPaint(1, Color.BLUE);
                    renderer.setSeriesStroke(1, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(1, true);
                    renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(1, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(2, Color.RED);
                    renderer.setSeriesStroke(2, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(2, true);
                    renderer.setSeriesShape(2, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(2, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    renderer.setSeriesPaint(3, Color.MAGENTA);
                    renderer.setSeriesStroke(3, new BasicStroke(1.5f));
                    renderer.setSeriesShapesVisible(3, true);
                    renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                    renderer.setSeriesFillPaint(3, Color.WHITE);
                    renderer.setUseFillPaint(true);

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" CONT.");
                    titleLabel.setToolTipText("CONTRIBUTION BY DEPTH FOR THE SELECTED NODE");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
                    enlargeBtn.setFocusable(false);
                    enlargeBtn.setBackground(Color.WHITE);
                    enlargeBtn.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            new Thread(new Runnable() {
                                @Override public void run() {
                                    enlarge();
                            }
                        }).start();
                    }});

                    JComboBox graphMenu = new JComboBox();
                    graphMenu.addItem("SELECT");
                    graphMenu.addItem("CONT.");
                    graphMenu.addItem("IN");
                    graphMenu.addItem("OUT");
                    graphMenu.addItem("DIFF.");
                    graphMenu.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphMenu.setFocusable(false);
                    graphMenu.setBackground(Color.WHITE);
                    graphMenu.setSelectedIndex(selectedGraph);
                    graphMenu.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            selectedGraph = graphMenu.getSelectedIndex();
                            decorate();;
                        }
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphMenu);
                    btnPanel.add(enlargeBtn);

                    JPanel menuPanel = new JPanel();
                    menuPanel.setLayout(new BorderLayout(0,0));
                    menuPanel.setBackground(Color.WHITE);
                    menuPanel.add(titlePanel, BorderLayout.WEST);
                    menuPanel.add(btnPanel, BorderLayout.CENTER);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MySingleNodeContributionByDepthLineChart reachTimeByDepthLineChart = new MySingleNodeContributionByDepthLineChart();

            JFrame f = new JFrame("NODE CONTRIBUTION BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 450));

            f.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
            f.pack();

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
