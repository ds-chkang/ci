package datamining.graph.stats.singlenode;

import datamining.graph.MyNode;
import datamining.utils.MyMathUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.TreeMap;

public class MySingleNodeHopCountDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public MySingleNodeHopCountDistributionLineChart() {
        this.decorate();
    }

    private void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
                setLayout(new BorderLayout(3,3));
                setBackground(Color.WHITE);

                MyNode selectedSingleNode = null;
                if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                    selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
                } else {
                    selectedSingleNode = MyVars.getViewer().selectedNode;
                }

                TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
                for (int s = 0; s < MyVars.seqs.length; s++) {
                    for (int itemsetIdx = 0; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                        String nn = MyVars.seqs[s][itemsetIdx].split(":")[0];
                        if (nn.equals(selectedSingleNode.getName())) {
                            int hopCount = (MyVars.seqs[s].length-(itemsetIdx+1));
                            if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                                nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount)+1);
                            } else {
                                nodeHopCountByNodeMap.put(hopCount, 1);
                            }
                        }
                    }
                }

                for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
                    if (nodeHopCountByNodeMap.containsKey(hopCount)) {
                        nodeHopCountByNodeMap.put(hopCount, nodeHopCountByNodeMap.get(hopCount) + 1);
                    } else {
                        nodeHopCountByNodeMap.put(hopCount, 1);
                    }
                }

                XYSeriesCollection dataset = new XYSeriesCollection();
                XYSeries hopSereis = new XYSeries("HOP");
                for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
                    hopSereis.add(hopCount, nodeHopCountByNodeMap.get(hopCount));
                }
                dataset.addSeries(hopSereis);

                JFreeChart chart = ChartFactory.createXYLineChart("", "HOP", "", dataset);
                chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
                chart.getXYPlot().setBackgroundPaint(Color.WHITE);
                chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
                chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
                chart.getXYPlot().getDomainAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chart.getXYPlot().getRangeAxis().setTickLabelFont(MyVars.tahomaPlainFont10);
                chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
                chart.setBackgroundPaint(Color.WHITE);

                XYPlot plot = (XYPlot)chart.getPlot();
                XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
                renderer.setSeriesPaint(0, Color.BLACK);
                renderer.setSeriesStroke(0, new BasicStroke(1.5f));
                renderer.setSeriesShapesVisible(0, true);
                renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
                renderer.setSeriesFillPaint(0, Color.WHITE);
                renderer.setUseFillPaint(true);

                ChartPanel chartPanel = new ChartPanel( chart );
                chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                JLabel titleLabel = new JLabel(" N. H.");
                titleLabel.setToolTipText("NODE HOP DISTRIBUTION");
                titleLabel.setFont(MyVars.tahomaBoldFont11);
                titleLabel.setBackground(Color.WHITE);
                titleLabel.setForeground(Color.DARK_GRAY);

                JPanel titlePanel = new JPanel();
                titlePanel.setBackground(Color.WHITE);
                titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                titlePanel.add(titleLabel);

                JButton enlargeBtn = new JButton("+");
                enlargeBtn.setBackground(Color.WHITE);
                enlargeBtn.setFont(MyVars.tahomaPlainFont10);
                enlargeBtn.setFocusable(false);
                enlargeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                enlarge();
                            }
                        }).start();
                    }
                });

                JPanel enlargePanel = new JPanel();
                enlargePanel.setBackground(Color.WHITE);
                enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3,3));
                enlargePanel.add(enlargeBtn);

                JPanel topPanel = new JPanel();
                topPanel.setBackground(Color.WHITE);
                topPanel.setLayout(new BorderLayout(3,3));
                topPanel.add(titlePanel, BorderLayout.WEST);
                topPanel.add(enlargePanel, BorderLayout.CENTER);

                setBackground(Color.WHITE);
                add(chartPanel, BorderLayout.CENTER);
                //if (instances == 0) {
                    add(topPanel, BorderLayout.NORTH);
               // }
                renderer.setBaseLegendTextFont(MyVars.tahomaPlainFont10);
                //instances++;
                revalidate();
                repaint();
            }
        });
    }

    public void enlarge() {
        JFrame frame = new JFrame(" MULTINODE HOP DISTRIBUTION");
        frame.setLayout(new BorderLayout(3,3));
        frame.getContentPane().add(new MySingleNodeHopCountDistributionLineChart(), BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(400, 300));
        frame.pack();
        frame.setVisible(true);
    }

    private JFreeChart setChart() {
        MyNode selectedSingleNode = null;
        if (MyVars.getViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
            selectedSingleNode = ((MyNode) MyVars.g.vRefs.get(MyVars.getViewer().vc.depthNodeNameSet.iterator().next()));
        } else {
            selectedSingleNode = MyVars.getViewer().selectedNode;
        }

        TreeMap<Integer, Integer> nodeHopCountByNodeMap = new TreeMap<>();
        for (int s = 0; s < MyVars.seqs.length; s++) {
            for (int itemsetIdx = 0; itemsetIdx < MyVars.seqs[s].length; itemsetIdx++) {
                String nn = MyVars.seqs[s][itemsetIdx].split(":")[0];
                if (nn.equals(selectedSingleNode.getName())) {
                    int hopLength = (MyVars.seqs[s].length - (itemsetIdx + 1));
                    if (hopLength > 0) {
                        if (nodeHopCountByNodeMap.containsKey(hopLength)) {
                            nodeHopCountByNodeMap.put(hopLength, nodeHopCountByNodeMap.get(hopLength) + 1);
                        } else {
                            nodeHopCountByNodeMap.put(hopLength, 1);
                        }
                    }
                }
            }
        }

        int totalLengths = 0;
        int totalCount = 0;
        for (Integer hopLength : nodeHopCountByNodeMap.keySet()) {
            totalLengths += nodeHopCountByNodeMap.get(hopLength)*hopLength;
            totalCount += nodeHopCountByNodeMap.get(hopLength);
        }


        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Integer hopCount : nodeHopCountByNodeMap.keySet()) {
            dataset.addValue(nodeHopCountByNodeMap.get(hopCount), "HOP", hopCount);
        }

        String plotTitle = "";
        String xaxis = " HOP LENGTH[AVG.: " + MySysUtil.formatAverageValue(MyMathUtil.twoDecimalFormat((double)totalLengths/totalCount)) + "]";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;
        return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
    }
}
