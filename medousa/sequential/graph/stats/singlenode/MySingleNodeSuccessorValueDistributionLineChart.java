package medousa.sequential.graph.stats.singlenode;

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
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;

public class MySingleNodeSuccessorValueDistributionLineChart extends JPanel {

    public static int instances = 0;
    private boolean MAXIMIZED;
    public MySingleNodeSuccessorValueDistributionLineChart() {
        this.decorate();
    }
    public void decorate() {
        try {
            removeAll();
            this.setLayout(new BorderLayout(5,5));
            this.setBackground(Color.WHITE);

            MyNode selectedSingleNode = null;
            if (MySequentialGraphVars.getSequentialGraphViewer().vc.selectedNodeNeighborNodeTypeSelector.isVisible()) {
                selectedSingleNode = ((MyNode) MySequentialGraphVars.g.vRefs.get(MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet.iterator().next()));
            } else if (MySequentialGraphVars.getSequentialGraphViewer().selectedNode != null){
                selectedSingleNode = MySequentialGraphVars.getSequentialGraphViewer().selectedNode;
            }

            Set<MyNode> successors = new HashSet<>(MySequentialGraphVars.g.getSuccessors(selectedSingleNode));
            Map<Integer, Integer> successorNodeValueMap = new HashMap<>();
            XYSeries successorValueSeries = new XYSeries("S. V.");

            int count = 0;

            if (successors != null) {
                for (MyNode n : successors) {
                    if (n.getCurrentValue() > 0) {
                        if (successorNodeValueMap.containsKey((int) n.getCurrentValue())) {
                            successorNodeValueMap.put((int) n.getCurrentValue(), successorNodeValueMap.get((int) n.getCurrentValue()) + 1);
                        } else {
                            successorNodeValueMap.put((int) n.getCurrentValue(), 1);
                        }

                        if (!MAXIMIZED && ++count > 8) {
                            break;
                        } else if (MAXIMIZED && ++count == 50){
                            break;
                        }
                    }
                }
            }

            for (Integer successorValue : successorNodeValueMap.keySet()) {
                successorValueSeries.add(successorValue, successorNodeValueMap.get(successorValue));
            }

            XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries(successorValueSeries);

            JFreeChart chart = ChartFactory.createXYLineChart("", "S. V.", "", dataset);
            chart.getTitle().setHorizontalAlignment(HorizontalAlignment.LEFT);
            chart.getXYPlot().setBackgroundPaint(Color.WHITE);
            chart.getXYPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getXYPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getTitle().setFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
            chart.getXYPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chart.getXYPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
            chart.getXYPlot().getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            chart.setBackgroundPaint(Color.WHITE);

            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
            renderer.setSeriesPaint(0, Color.BLACK);
            renderer.setSeriesStroke(0, new BasicStroke(1.5f));
            renderer.setSeriesShapesVisible(0, true);
            renderer.setSeriesShape(0, new Ellipse2D.Double(-2.0, -2.0, 4.0, 4.0));
            renderer.setSeriesFillPaint(0, Color.WHITE);
            renderer.setUseFillPaint(true);

            ChartPanel chartPanel = new ChartPanel( chart );
            chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

            JLabel titleLabel = new JLabel(" S. V.");
            titleLabel.setToolTipText("SUCCESSOR VALUE DISTRIBUTION FOR THE SELECTED NODE");
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

            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont10);
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
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" SUCCESSOR VALUE DISTRIBUTION FOR THE SELECTED NODE");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 300));
            f.getContentPane().add(new MySingleNodeSuccessorValueDistributionLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
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
            pb.updateValue(100, 100);
            pb.dispose();
            f.setVisible(true);
            f.setAlwaysOnTop(false);
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

}
