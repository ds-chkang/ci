package medousa.sequential.graph.stats.depthnode;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart
extends JPanel{

    public static int instances = 0;
    public MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                setAllCharts();
            }
        });
    }

        public void setAllCharts() {
        try {
            removeAll();
            setLayout(new BorderLayout(5, 5));
            setBackground(Color.WHITE);

            XYSeries successorSeries = new XYSeries("S.");
            XYSeries predecessorSeries = new XYSeries("P.");

            XYSeriesCollection dataset = new XYSeriesCollection();
            Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();

            if (MySequentialGraphVars.currentGraphDepth == 1 && MySequentialGraphVars.isSupplementaryOn) {
                int totalPredecessors = 0;
                for (MyNode n : nodes) {
                    if (n.getName().contains("x")) {
                        totalPredecessors++;
                    }
                }
                successorSeries.add(1, MySequentialGraphVars.g.getVertexCount()-totalPredecessors);
                predecessorSeries.add(1, 0);

                for (int i = 2; i <= MySequentialGraphVars.mxDepth; i++) {
                    successorSeries.add(i, 0);
                    predecessorSeries.add(i, 0);
                }
            } else {
                Set<String> successors = new HashSet<>();
                Set<String> predecessors = new HashSet<>();
                for (int s = 0; s < MySequentialGraphVars.seqs.length; ++s) {
                    if (MySequentialGraphVars.seqs[s].length < MySequentialGraphVars.currentGraphDepth) continue;
                    for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                        if ((i + 1) == MySequentialGraphVars.currentGraphDepth) {
                            if (MySequentialGraphVars.seqs[s].length > MySequentialGraphVars.currentGraphDepth) {
                                successors.add(MySequentialGraphVars.seqs[s][MySequentialGraphVars.currentGraphDepth].split(":")[0]);
                            }
                            predecessors.add(MySequentialGraphVars.seqs[s][i-1].split(":")[0]);
                        }
                    }
                }

                for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                    if (i == MySequentialGraphVars.currentGraphDepth) {
                        successorSeries.add(MySequentialGraphVars.currentGraphDepth, successors.size());
                        predecessorSeries.add(MySequentialGraphVars.currentGraphDepth, predecessors.size());
                    } else {
                        successorSeries.add(i, 0);
                        predecessorSeries.add(i, 0);
                    }
                }
            }

            dataset.addSeries(successorSeries);
            dataset.addSeries(predecessorSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "P. & S.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);

            renderer.setSeriesPaint(1, Color.BLUE);
            renderer.setSeriesStroke(2, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(1, true);
            renderer.setSeriesShape(1, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(1, Color.WHITE);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(560, 367));

            JLabel titleLabel = new JLabel(" PRED. & SUCC.");
            titleLabel.setToolTipText("PREDECESSOR & SUCCESSORS BY DEPTH");
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
                    new Thread(new Runnable() {
                        @Override public void run() {
                            enlarge();
                        }
                    }).start();
                }
            });


            JPanel menuPanel = new JPanel();
            menuPanel.setBackground(Color.WHITE);
            menuPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout(0, 0));
            topPanel.setBackground(Color.WHITE);
            topPanel.add(menuPanel, BorderLayout.CENTER);
            menuPanel.add(enlargeBtn);
            topPanel.add(titlePanel, BorderLayout.WEST);

            add(topPanel, BorderLayout.NORTH);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
            add(chartPanel, BorderLayout.CENTER);

            revalidate();
            repaint();
        } catch (Exception ex) {ex.printStackTrace();}
    }


    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            JFrame f = new JFrame("PREDECESSOR AND SUCCESSOR BY DEPTH DISTRIBUTION");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(450, 350));
            f.getContentPane().add(new MyDepthLevelPredecessorsAndSuccessorsByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(false);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                }
            });
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
