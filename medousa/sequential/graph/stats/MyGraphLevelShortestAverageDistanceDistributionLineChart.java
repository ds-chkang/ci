package medousa.sequential.graph.stats;

import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.HorizontalAlignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MyGraphLevelShortestAverageDistanceDistributionLineChart
extends JPanel {

    public static int instances = 0;
    public JComboBox chartMenu;
    private XYSeries valueSeries;

    public MyGraphLevelShortestAverageDistanceDistributionLineChart() {
        decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setNodeValueLineChart();
            }
        });
    }

    private TreeMap<Long, Long> createNodeValueMap() {
        TreeMap<Long, Long> valueMap = new TreeMap<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (n.getCurrentValue() == 0) continue;
            long value = (long) n.avgShortestDistance;
            if (value == 0) continue;
            if (valueMap.containsKey(value)) {
                valueMap.put(value, valueMap.get(value) + 1);
            } else {
                valueMap.put(value, 1L);
            }
        }
        return valueMap;
    }

    private void setNodeValueLineChart() {
        removeAll();
        setLayout(new BorderLayout(3, 3));
        setBackground(Color.WHITE);

        TreeMap<Long, Long> valueMap = createNodeValueMap();
        XYSeriesCollection dataset = new XYSeriesCollection();
        this.valueSeries = new XYSeries("AVG. SHORTEST D.");
        for (Long value : valueMap.keySet()) {
            this.valueSeries.add(value, valueMap.get(value));
        }
        dataset.addSeries(this.valueSeries);

        JFreeChart chart = ChartFactory.createXYLineChart("", "", "", dataset);
        chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
        chart.getXYPlot().setBackgroundPaint(Color.WHITE);
        chart.getXYPlot().setDomainGridlinePaint(Color.GRAY);
        chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
        chart.getXYPlot().getDomainAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
        chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        chart.setBackgroundPaint(Color.WHITE);

        XYPlot plot = (XYPlot) chart.getPlot();
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, Color.DARK_GRAY);
        renderer.setSeriesStroke(0, new BasicStroke(0.9f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShape(0, new Ellipse2D.Double(-1.0, -1.0, 2.0, 2.0));
        renderer.setSeriesFillPaint(0, Color.WHITE);
        renderer.setUseFillPaint(true);
        renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(350, 367));

        JLabel titleLabel = new JLabel(" AVG. SHORTEST D.");
        titleLabel.setToolTipText("AVERAGE SHORTEST DISTANCE DISTRIBUTION");
        titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont11);
        titleLabel.setBackground(Color.WHITE);
        titleLabel.setForeground(Color.DARK_GRAY);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
        titlePanel.add(titleLabel);

        JButton enlargeBtn = new JButton("+");
        enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont11);
        enlargeBtn.setBackground(Color.WHITE);
        enlargeBtn.setFocusable(false);
        enlargeBtn.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                new Thread(new Runnable() {
                    @Override public void run() {
                        enlarge();
                    }
                }).start();
            }
        });

        JPanel enlargePanel = new JPanel();
        enlargePanel.setBackground(Color.WHITE);
        enlargePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
        enlargePanel.add(enlargeBtn);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(3, 3));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(titlePanel, BorderLayout.WEST);
        topPanel.add(enlargePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);
        chart.removeLegend();

        revalidate();
        repaint();
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame(" AVERAGE SHORTEST DISTANCE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyGraphLevelNodeValueDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }
                @Override public void mouseExited(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }
            });
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
