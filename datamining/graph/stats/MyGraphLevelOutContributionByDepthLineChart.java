package datamining.graph.stats;

import datamining.main.MyProgressBar;
import datamining.graph.MyNode;
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
import java.util.Collection;

public class MyGraphLevelOutContributionByDepthLineChart
extends JPanel {

    public static int instances = 0;
    public MyGraphLevelOutContributionByDepthLineChart() {
        this.decorate();
    }
    public void decorate() {
        try {
            this.removeAll();
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            Collection<MyNode> nodes = MyVars.g.getVertices();
            XYSeries nodeOutContributionByDepthSeries = new XYSeries("OUT-CONT.");
            for (int i = 1; i <= MyVars.mxDepth; i++) {
                int totalOutCont = 0;
                for (MyNode n : nodes) {
                    if (n.getNodeDepthInfo(i) != null) {
                        totalOutCont += n.getNodeDepthInfo(i).getOutContribution();
                    }
                }
                nodeOutContributionByDepthSeries.add(i, totalOutCont);
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(nodeOutContributionByDepthSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "DEPTH", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.BLUE);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );
            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

            JLabel titleLabel = new JLabel(" OUT-CONTRIBUTION BY DEPTH");
            titleLabel.setFont(MyVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            JButton enlargeBtn = new JButton("ENLARGE");
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

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
            btnPanel.add(enlargeBtn);

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout(0,0));
            menuPanel.setBackground(Color.WHITE);
            menuPanel.add(titlePanel, BorderLayout.WEST);

            if (instances == 0) {
                menuPanel.add(btnPanel, BorderLayout.CENTER);
            }
            renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
            this.add(menuPanel, BorderLayout.NORTH);
            this.add(chartPanel, BorderLayout.CENTER);
            instances++;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("OUT-CONTRIBUTION BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(600, 400));
        pb.updateValue(20, 100);

        MyGraphLevelOutContributionByDepthLineChart outContributionByDepthLineChart = new MyGraphLevelOutContributionByDepthLineChart();
        frame.getContentPane().add(outContributionByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "NODE STATISTICS");
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        titledBorder.setTitleFont(new Font("Arial", Font.PLAIN, 0));
        titledBorder.setTitleColor(Color.DARK_GRAY);
        outContributionByDepthLineChart.setBorder(titledBorder);
        pb.updateValue(100,100);
        pb.dispose();

        frame.setCursor(Cursor.HAND_CURSOR);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

}
