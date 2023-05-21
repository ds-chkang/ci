package medousa.direct.graph.path;

import medousa.MyProgressBar;
import medousa.direct.graph.MyDirectGraph;
import medousa.direct.graph.MyDirectNode;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
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
import java.util.Collection;
import java.util.TreeMap;

public class MyDirectGraphPathGraphNodeContributionDistributionLineChart extends JPanel {

    private MyDirectGraph directedSparseMultigraph;
    public MyDirectGraphPathGraphNodeContributionDistributionLineChart(MyDirectGraph directedSparseMultigraph) {
        this.directedSparseMultigraph = directedSparseMultigraph;
        this.decorate();
    }
    public void decorate() {
        try {
            removeAll();
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            TreeMap<Long, Integer> nodeValueMap = new TreeMap<>();
            Collection<MyDirectNode> nodes = this.directedSparseMultigraph.getVertices();
            int count = 0;
            long totalContribution = 0L;
            for (MyDirectNode n : nodes) {
                if (n.getContribution() > 0) {
                    long value = n.getContribution();
                    totalContribution += value;
                    count++;
                      if (nodeValueMap.containsKey(value)) {nodeValueMap.put(value, nodeValueMap.get(value) + 1);}
                    else {nodeValueMap.put(value, 1);}
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            XYSeries series = null;
            if (nodeValueMap.size() == 0) {
                series = new XYSeries("[AVG.: 0.00]");
                series.add(0, 0);
            } else {
                series = new XYSeries("[AVG.:" + MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((double)totalContribution/count)) + "]");
                for (Long nodeValue : nodeValueMap.keySet()) {
                    series.add(nodeValue, nodeValueMap.get(nodeValue));
                }
            }


            dataset.addSeries(series);

            JFreeChart chart = ChartFactory.createXYLineChart("", "N. CONT.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.tahomaPlainFont11);
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
            renderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont11);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );
            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

            JLabel titleLabel = new JLabel(" NODE CONTRIBUTION");
            titleLabel.setFont(MyDirectGraphVars.tahomaBoldFont11);
            titleLabel.setBackground(Color.WHITE);
            titleLabel.setForeground(Color.DARK_GRAY);

            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(Color.WHITE);
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            titlePanel.add(titleLabel);

            JButton enlargeBtn = new JButton("+");
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
            this.add(menuPanel, BorderLayout.NORTH);

            this.add(chartPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" NODE CONTRIBUTION DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 450));
            f.getContentPane().add(this, BorderLayout.CENTER);
            f.pack();
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
