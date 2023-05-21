package medousa.sequential.graph.stats.depthnode;

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

public class MyDepthLevelEndingNodeValueHistogramDistributionLineChart
extends JPanel
implements ActionListener {

    private static boolean MAXIMIZED;
    public static int instances = 0;
    private ArrayList<Color> colors;
    private Random rand = new Random();

    public MyDepthLevelEndingNodeValueHistogramDistributionLineChart() {
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
                    LinkedHashMap<String, Long> endingNodeValueMap = new LinkedHashMap<>();
                    if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet != null) {
                        for (String selectedNode : MySequentialGraphVars.getSequentialGraphViewer().vc.depthNodeNameSet) {
                            for (int s = 0; s < MySequentialGraphVars.seqs.length; s++) {
                                for (int i = 0; i < MySequentialGraphVars.seqs[s].length; i++) {
                                    String n = MySequentialGraphVars.seqs[s][i].split(":")[0];
                                    if ((i + 1) != MySequentialGraphVars.seqs[s].length && n.equals(selectedNode)) {
                                        String endingNode = MySequentialGraphVars.seqs[s][MySequentialGraphVars.seqs[s].length - 1].split(":")[0];
                                        endingNode = (endingNode.contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(endingNode) : MySequentialGraphSysUtil.getDecodedNodeName(endingNode));
                                        if (endingNodeValueMap.containsKey(endingNode)) {
                                            endingNodeValueMap.put(endingNode, endingNodeValueMap.get(endingNode) + 1);
                                        } else {
                                            endingNodeValueMap.put(endingNode, 1L);
                                        }
                                        final float hue = rand.nextFloat();
                                        final float saturation = 0.9f;
                                        final float luminance = 1.0f;
                                        Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                                        colors.add(randomColor);
                                        count++;
                                        break;
                                    }
                                }
                                if (count == 6 && !MAXIMIZED) {
                                    break;
                                } else if (count == 50 && MAXIMIZED) {
                                    break;
                                }
                            }
                            if (count == 6 && !MAXIMIZED) {
                                break;
                            } else if (count == 50 && MAXIMIZED) {
                                break;
                            }
                        }
                    }
                    endingNodeValueMap = MySequentialGraphSysUtil.sortMapByLongValue(endingNodeValueMap);

                    CategoryDataset dataset = new DefaultCategoryDataset();
                    for (String label : endingNodeValueMap.keySet()) {
                        ((DefaultCategoryDataset) dataset).addValue(endingNodeValueMap.get(label), label, "");
                    }

                    // Create a bar chart with the dataset
                    JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
                    chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
                    chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                    chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
                    BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
                    renderer.setBarPainter(new StandardBarPainter());
                    // Set the colors for each data item in the series
                    for (int i = 0; i < dataset.getColumnCount(); i++) {
                        renderer.setSeriesPaint(i, colors.get(i));
                    }
                    renderer.setMaximumBarWidth(0.07);
                    renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);
                    // Create a ChartPanel and display it
                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize(new Dimension(350, 367));

                    JLabel titleLabel = new JLabel(" ENDING. N.");
                    titleLabel.setToolTipText("ENDING NODES");
                    titleLabel.setFont(MySequentialGraphVars.tahomaBoldFont12);
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

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setBackground(Color.WHITE);
                    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
                    buttonPanel.add(enlargeBtn);

                    JPanel topPanel = new JPanel();
                    topPanel.setLayout(new BorderLayout(3, 3));
                    topPanel.setBackground(Color.WHITE);
                    topPanel.add(titlePanel, BorderLayout.WEST);
                    topPanel.add(buttonPanel, BorderLayout.EAST);
                    add(topPanel, BorderLayout.NORTH);
                    add(chartPanel, BorderLayout.CENTER);

                    revalidate();
                    repaint();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" ENDING NODE VALUE DISTRIBUTION");
            f.setLayout(new BorderLayout(3, 3));
            f.getContentPane().add(new MyDepthLevelEndingNodeValueHistogramDistributionLineChart(), BorderLayout.CENTER);
            f.setPreferredSize(new Dimension(450, 350));
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
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

    @Override public void actionPerformed(ActionEvent e) {

    }

}
