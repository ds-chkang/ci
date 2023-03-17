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
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public class MySingleNodeContributionByDepthLineChart
extends JPanel {

    public static int instances = 0;
    JComboBox graphSelectionOption;
    XYSeries contributionByDepthSeries = new XYSeries("CONT.");
    XYSeries inContributionByDepthSeries = new XYSeries("IN");
    XYSeries outContributionByDepthSeries = new XYSeries("OUT");
    int selectedOption;
    public MySingleNodeContributionByDepthLineChart() {
        this.decorate();
    }
    private void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    MyNode selectedSingleNode = null;
                    if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                        selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                    } else {
                        selectedSingleNode = MyVars.getViewer().selectedNode;
                    }

                    if (MyVars.currentGraphDepth > 0) {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (MyVars.currentGraphDepth == i) {
                                if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                                    contributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getContribution());
                                    inContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution());
                                    outContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                                } else {
                                    contributionByDepthSeries.add(i, 0);
                                    inContributionByDepthSeries.add(i, 0);
                                    outContributionByDepthSeries.add(i, 0);
                                }
                            } else {
                                contributionByDepthSeries.add(i, 0);
                                inContributionByDepthSeries.add(i, 0);
                                outContributionByDepthSeries.add(i, 0);
                            }
                        }
                    } else {
                        for (int i = 1; i <= MyVars.mxDepth; i++) {
                            if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                                contributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getContribution());
                                inContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getInContribution());
                                outContributionByDepthSeries.add(i, selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                            } else {
                                contributionByDepthSeries.add(i, 0);
                                inContributionByDepthSeries.add(i, 0);
                                outContributionByDepthSeries.add(i, 0);
                            }
                        }
                    }

                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(contributionByDepthSeries);
                    dataset.addSeries(inContributionByDepthSeries);
                    dataset.addSeries(outContributionByDepthSeries);

                    JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
                    chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                    chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                    chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                    chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont11);
                    chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.getXYPlot().getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                    chart.setBackgroundPaint(Color.WHITE);

                    XYPlot plot = (XYPlot) chart.getPlot();
                    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                    renderer.setSeriesPaint(0, Color.RED);
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

                    renderer.setSeriesPaint(2, Color.DARK_GRAY);
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

                    JLabel titleLabel = new JLabel(" CONTRIBUTION");
                    titleLabel.setToolTipText("CONTRIBUTION BY DEPTH FOR THE SELECTED NODE");
                    titleLabel.setFont(MyVars.tahomaBoldFont11);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MyVars.tahomaPlainFont10);
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

                    graphSelectionOption = new JComboBox();
                    graphSelectionOption.setFont(MyVars.tahomaPlainFont10);
                    graphSelectionOption.setFocusable(false);
                    graphSelectionOption.setBackground(Color.WHITE);
                    graphSelectionOption.addItem("SELECT");
                    graphSelectionOption.addItem("CONT.");
                    graphSelectionOption.addItem("IN");
                    graphSelectionOption.addItem("OUT");
                    graphSelectionOption.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            graphSelectionOption.setSelectedIndex(graphSelectionOption.getSelectedIndex());
                            setLineGraphRendering(renderer, graphSelectionOption.getSelectedIndex(), dataset);
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
                    renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont11);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);
                    instances++;
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void setLineGraphRendering(XYLineAndShapeRenderer renderer, int selected, XYSeriesCollection dataset) {
        Color colors [] = {Color.RED, Color.BLUE, Color.DARK_GRAY, Color.decode("#59A869"), Color.BLACK, Color.BLACK};
        if (selected == 0) {
            dataset.removeAllSeries();
            dataset.addSeries(contributionByDepthSeries);
            dataset.addSeries(inContributionByDepthSeries);
            dataset.addSeries(outContributionByDepthSeries);

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

            renderer.setSeriesPaint(3, colors[3]);
            renderer.setSeriesStroke(3, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(3, true);
            renderer.setSeriesShape(3, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(3, Color.WHITE);

        } else {
            dataset.removeAllSeries();
            XYSeriesCollection tmp = new XYSeriesCollection();
            dataset.removeAllSeries();
            tmp.addSeries(contributionByDepthSeries);
            tmp.addSeries(inContributionByDepthSeries);
            tmp.addSeries(outContributionByDepthSeries);
            dataset.addSeries(tmp.getSeries(selected-1));

            renderer.setSeriesPaint(0, colors[selected-1]);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesLinesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("CONTRIBUTION BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 450));
        pb.updateValue(20, 100);

        MySingleNodeContributionByDepthLineChart reachTimeByDepthLineChart = new MySingleNodeContributionByDepthLineChart();
        frame.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        reachTimeByDepthLineChart.setBorder(titledBorder);
        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
