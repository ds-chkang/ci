package medousa.sequential.graph.stats.multinode;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
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

public class MyMultiLevelSharedPredecessorAppearancesByDepthLineChart
extends JPanel {

    public static int instances = 0;
    public MyMultiLevelSharedPredecessorAppearancesByDepthLineChart() {
        this.decorate();
    }
    public void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);
                    XYSeries sharedNodesByDetphSeries = new XYSeries("NO. OF SHARED PREDECESSORS APPEARANCES");
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        int sharedPredecessors = 0;
                        for (MyNode s : MySequentialGraphVars.getSequentialGraphViewer().sharedPredecessors) {
                            if (s.getNodeDepthInfoMap().containsKey(i)) {
                                sharedPredecessors++;
                            }
                        }
                        sharedNodesByDetphSeries.add(i, sharedPredecessors);
                    }
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(sharedNodesByDetphSeries);
                    JFreeChart chart = ChartFactory.createXYLineChart("", "SHARED P.", "", dataset);
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

                    ChartPanel chartPanel = new ChartPanel( chart );
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" SHARED P.");
                    titleLabel.setToolTipText("SHARED PREDECESSORS BY DEPTH FOR THE SELECTED NODES");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
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
                        new Thread(new Runnable() {@Override public void run() {
                            enlarge();
                        }}).start();}
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
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    add(menuPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("SHARED PREDECESSORS APPEARANCES BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 450));

            MyMultiLevelSharedPredecessorAppearancesByDepthLineChart reachTimeByDepthLineChart = new MyMultiLevelSharedPredecessorAppearancesByDepthLineChart();
            f.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
            f.pack();

            f.setAlwaysOnTop(true);
            f.setCursor(Cursor.HAND_CURSOR);
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
