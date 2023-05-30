package medousa.sequential.graph.stats.singlenode;

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

public class MySingleNodeAppearanceByDepthLineChart
extends JPanel {

    public MySingleNodeAppearanceByDepthLineChart() {
        this.decorate();
    }
    public void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);
                    XYSeries series = new XYSeries("NO. OF APPEARANCES BY DEPTH");
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        if (MySequentialGraphVars.getSequentialGraphViewer().singleNode.getNodeDepthInfoMap().containsKey(i)) {
                            series.add(i, MySequentialGraphVars.getSequentialGraphViewer().singleNode.getNodeDepthInfo(i).getContribution());
                        } else {
                            series.add(i, 0);
                        }
                    }
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    dataset.addSeries(series);
                    JFreeChart chart = ChartFactory.createXYLineChart("", "NO. OF APPEARANCES BY DEPTH", "", dataset);
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

                    JLabel titleLabel = new JLabel(" NO. OF A.");
                    titleLabel.setToolTipText("NO. OF APPEARANCES OF THE SELECTED NDOE BY DEPTH");
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
            decorate();
            ex.printStackTrace();
        }
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        JFrame frame = new JFrame("NO. OF APPEARANCES OF THE SELECTED NODE BY DEPTH");
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.WHITE);
        frame.setPreferredSize(new Dimension(400, 450));
        pb.updateValue(20, 100);

        MySingleNodeAppearanceByDepthLineChart reachTimeByDepthLineChart = new MySingleNodeAppearanceByDepthLineChart();
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
