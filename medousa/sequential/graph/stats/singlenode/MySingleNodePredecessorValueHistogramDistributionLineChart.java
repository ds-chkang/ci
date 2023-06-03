package medousa.sequential.graph.stats.singlenode;

import medousa.sequential.graph.MyNode;
import medousa.MyProgressBar;
import medousa.sequential.utils.MySequentialGraphSysUtil;
import medousa.sequential.utils.MySequentialGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class MySingleNodePredecessorValueHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    public static int instances = 0;
    private ArrayList<Color> colors;
    private Random rand = new Random();
    private boolean MAXIMIZED;

    public MySingleNodePredecessorValueHistogramDistributionLineChart() {
        this.decorate();
    }

    public void decorate() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    removeAll();
                    setLayout(new BorderLayout(3, 3));
                    setBackground(Color.WHITE);

                    int count = 0;
                    colors = new ArrayList<>();
                    LinkedHashMap<String, Long> predecessorNodeValueMap = new LinkedHashMap<>();
                    for (MyNode predecessor : MySequentialGraphVars.getSequentialGraphViewer().singleNodePredecessors) {
                        String pName = (predecessor.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(predecessor.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(predecessor.getName()));
                        predecessorNodeValueMap.put(pName, (long)predecessor.getCurrentValue());
                        final float hue = rand.nextFloat();
                        final float saturation = 0.9f;
                        final float luminance = 1.0f;
                        Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                        colors.add(randomColor);

                        if (!MAXIMIZED && ++count == 6) {
                            break;
                        } else if (MAXIMIZED && ++count == 50) {
                            break;
                        }
                    }

                    predecessorNodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(predecessorNodeValueMap);
                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : predecessorNodeValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(predecessorNodeValueMap.get(label), label, "");
                    }

                    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                    chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
                    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont10);
                    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                    renderer.setBarPainter(new StandardBarPainter());
                    for (int i = 0; i < dataset.getColumnCount(); i++) {
                        renderer.setSeriesPaint(i, colors.get(i));
                    }
                    renderer.setMaximumBarWidth(0.09);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont10);
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" P. V.");
                    titleLabel.setToolTipText("PREDECESSOR VALUE DISTRIBUTION");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
                    titleLabel.setBackground(Color.WHITE);
                    titleLabel.setForeground(Color.DARK_GRAY);

                    JPanel titlePanel = new JPanel();
                    titlePanel.setBackground(Color.WHITE);
                    titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3, 3));
                    titlePanel.add(titleLabel);

                    JButton enlargeBtn = new JButton("+");
                    enlargeBtn.setFont(MySequentialGraphVars.tahomaPlainFont10);
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

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    btnPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(btnPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                } catch (Exception ex) {}
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" PREDECESSOR VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MySingleNodePredecessorValueHistogramDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
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
        } catch (Exception ex) {
            MAXIMIZED = false;
            pb.updateValue(100, 100);
            pb.dispose();
        }
    }

    @Override public void actionPerformed(ActionEvent e) {

    }

}
