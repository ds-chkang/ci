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

public class MySingleNodeOutContributionByDepthLineChart
extends JPanel {

    public static int instances = 0;
    public MySingleNodeOutContributionByDepthLineChart() {
        this.decorate();
    }

    private void decorate() {
        try {
            removeAll();
            setLayout(new BorderLayout(5,5));
            setBackground(Color.WHITE);

            MyNode selectedSingleNode = null;
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
            } else {
                selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().singleNode;
            }

                Map<Integer, Integer> outContributionMap = new HashMap<>();
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (selectedSingleNode.getNodeDepthInfoMap().containsKey(i)) {
                    outContributionMap.put(i, selectedSingleNode.getNodeDepthInfo(i).getOutContribution());
                }
            }

            XYSeries outContributionValueSeries = new XYSeries("OUT-CONTRIBUTION");
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                if (outContributionMap.get(i) != null) {
                    outContributionValueSeries.add(i, outContributionMap.get(i));
                } else {
                    outContributionValueSeries.add(i, 0);
                }
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(outContributionValueSeries);

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
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );

            JLabel titleLabel = new JLabel("OUT-CONTRIBUTION");
            titleLabel.setToolTipText("OUT-CONTRIBUTION BY DEPTH FOR THE SELECTED NODE");
            titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
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
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            this.add(menuPanel, BorderLayout.NORTH);
            this.add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("OUT-CONTRIBUTION BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 450));
        pb.updateValue(20, 100);

        MySingleNodeOutContributionByDepthLineChart reachTimeByDepthLineChart = new MySingleNodeOutContributionByDepthLineChart();
        frame.getContentPane().add(reachTimeByDepthLineChart, BorderLayout.CENTER);
        frame.pack();
        pb.updateValue(60, 100);

        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(blackline, "");
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
