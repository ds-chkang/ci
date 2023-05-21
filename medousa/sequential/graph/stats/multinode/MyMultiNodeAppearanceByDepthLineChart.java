package medousa.sequential.graph.stats.multinode;

import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import medousa.MyProgressBar;
import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphSysUtil;
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

public class MyMultiNodeAppearanceByDepthLineChart
extends JPanel {

    public static int instances = 0;
    private ArrayList<Color> colors;
    private Random rand = new Random();
    private boolean MAXIMIZED;

    public MyMultiNodeAppearanceByDepthLineChart() {
        this.decorate();
    }
    public synchronized void decorate() {
        try {
            SwingUtilities.invokeLater(new Runnable() {
                @Override public void run() {
                    removeAll();
                    setLayout(new BorderLayout(5,5));
                    setBackground(Color.WHITE);

                    JComboBox graphSelectionOption = new JComboBox();
                    graphSelectionOption.setFont(MySequentialGraphVars.tahomaPlainFont11);
                    graphSelectionOption.setFocusable(false);
                    graphSelectionOption.setBackground(Color.WHITE);
                    graphSelectionOption.addItem("");
                    Set<Integer> depths = new HashSet<>();
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        for (MyNode selectedNode : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                            if (selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                                depths.add(i);
                            }
                        }
                    }
                    for (Integer depth : depths) {graphSelectionOption.addItem("" + depth);}

                    graphSelectionOption.addActionListener(new ActionListener() {
                        @Override public void actionPerformed(ActionEvent e) {
                            if (graphSelectionOption.getSelectedIndex() > 0) {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        MyProgressBar pb = new MyProgressBar(false);
                                        int depth = Integer.parseInt(graphSelectionOption.getSelectedItem().toString());
                                        setDepthNodeBarChart(depth, graphSelectionOption.getSelectedIndex());
                                        pb.updateValue(100, 100);
                                        pb.dispose();
                                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                                    }
                                }).start();
                            } else {
                                new Thread(new Runnable() {
                                    @Override public void run() {
                                        MyProgressBar pb = new MyProgressBar(false);
                                        decorate();
                                        pb.updateValue(100, 100);
                                        pb.dispose();
                                        MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                        MySequentialGraphVars.getSequentialGraphViewer().repaint();
                                    }
                                }).start();
                            }
                        }
                    });

                    colors = new ArrayList<>();
                    XYSeriesCollection dataset = new XYSeriesCollection();
                    XYSeries nodeAppearanceSereis = new XYSeries("APPEARANCES OF SELECTED NODES");
                    for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                        int multiNodeCnt = 0;
                        for (MyNode selectedNode : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                            if (selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                                multiNodeCnt++;
                                final float hue = rand.nextFloat();
                                final float saturation = 0.9f;
                                final float luminance = 1.0f;
                                Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                                colors.add(randomColor);
                                if (!MAXIMIZED && multiNodeCnt == 6) {
                                    break;
                                } else if (MAXIMIZED && multiNodeCnt == 200) {
                                    break;
                                }
                            }
                        }
                        nodeAppearanceSereis.add(i, multiNodeCnt);
                    }
                    dataset.addSeries(nodeAppearanceSereis);

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

                    ChartPanel chartPanel = new ChartPanel(chart);
                    chartPanel.setPreferredSize( new Dimension( 350 , 367 ) );

                    JLabel titleLabel = new JLabel(" SELECTED N.");
                    titleLabel.setToolTipText("APPEARANCES OF SELECTED NDOES BY DEPTH");
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
                        new Thread(new Runnable() {@Override public void run() {
                            enlarge();
                        }}).start();}
                    });

                    JPanel btnPanel = new JPanel();
                    btnPanel.setBackground(Color.WHITE);
                    btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
                    btnPanel.add(graphSelectionOption);
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
            ex.printStackTrace();
        }
    }

    private void setDepthNodeBarChart(int selectedDepth, int selectedIndex) {
        try {
            removeAll();
            setLayout(new BorderLayout(3, 3));
            setBackground(Color.WHITE);

            JComboBox graphSelectionOption = new JComboBox();
            graphSelectionOption.setFont(MySequentialGraphVars.tahomaPlainFont11);
            graphSelectionOption.setFocusable(false);
            graphSelectionOption.setBackground(Color.WHITE);
            graphSelectionOption.addItem("");
            Set<Integer> depths = new HashSet<>();
            for (int i = 1; i <= MySequentialGraphVars.mxDepth; i++) {
                for (MyNode selectedNode : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                    if (selectedNode.getNodeDepthInfoMap().containsKey(i)) {
                        depths.add(i);
                    }
                }
            }
            for (Integer depth : depths) {graphSelectionOption.addItem("" + depth);}
            graphSelectionOption.setSelectedIndex(selectedIndex);

            graphSelectionOption.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (graphSelectionOption.getSelectedIndex() > 0) {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MyProgressBar pb = new MyProgressBar(false);
                                int depth = Integer.parseInt(graphSelectionOption.getSelectedItem().toString());
                                setDepthNodeBarChart(depth, graphSelectionOption.getSelectedIndex());
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                MySequentialGraphVars.getSequentialGraphViewer().repaint();
                            }
                        }).start();
                    } else {
                        new Thread(new Runnable() {
                            @Override public void run() {
                                MyProgressBar pb = new MyProgressBar(false);
                                decorate();
                                pb.updateValue(100, 100);
                                pb.dispose();
                                MySequentialGraphVars.getSequentialGraphViewer().revalidate();
                                MySequentialGraphVars.getSequentialGraphViewer().repaint();
                            }
                        }).start();
                    }
                }
            });

            colors = new ArrayList<>();
            LinkedHashMap<String, Long> multiNodeAppearanceMap = new LinkedHashMap<>();
            int multiNodeCnt = 0;
            for (MyNode selectedNode : MySequentialGraphVars.getSequentialGraphViewer().multiNodes) {
                if (selectedNode.getNodeDepthInfoMap().containsKey(selectedDepth)) {
                    multiNodeCnt++;
                    final float hue = rand.nextFloat();
                    final float saturation = 0.9f;
                    final float luminance = 1.0f;
                    Color randomColor = Color.getHSBColor(hue, saturation, luminance);
                    colors.add(randomColor);
                    String node = (selectedNode.getName().contains("x") ? MySequentialGraphSysUtil.getDecodeVariableNodeName(selectedNode.getName()) : MySequentialGraphSysUtil.getDecodedNodeName(selectedNode.getName()));
                    multiNodeAppearanceMap.put(node, (long) selectedNode.getCurrentValue());
                    if (!MAXIMIZED && multiNodeCnt == 6) {
                        break;
                    } else if (MAXIMIZED && multiNodeCnt == 200) {
                        break;
                    }
                }
            }
            multiNodeAppearanceMap = MySequentialGraphSysUtil.sortMapByLongValue(multiNodeAppearanceMap);

            CategoryDataset dataset = new DefaultCategoryDataset();
            for (String label : multiNodeAppearanceMap.keySet()) {
                ((DefaultCategoryDataset) dataset).addValue(multiNodeAppearanceMap.get(label), label, "");
            }

            JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset);
            chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chart.getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chart.getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chart.getCategoryPlot().getDomainAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);
            chart.getCategoryPlot().getRangeAxis().setTickLabelFont(MySequentialGraphVars.tahomaPlainFont11);

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setBarPainter(new StandardBarPainter());
            for (int i = 0; i < dataset.getColumnCount(); i++) {
                renderer.setSeriesPaint(i, colors.get(i));
            }
            renderer.setMaximumBarWidth(0.09);
            renderer.setBaseLegendTextFont(MySequentialGraphVars.tahomaPlainFont11);

            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new Dimension(350, 367));

            JLabel titleLabel = new JLabel(" SELECTED N.");
            titleLabel.setToolTipText("SELECTED NODES BY DEPTH");
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

            JPanel btnPanel = new JPanel();
            btnPanel.setBackground(Color.WHITE);
            btnPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 3));
            btnPanel.add(graphSelectionOption);
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

    public void enlarge() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            MAXIMIZED = true;
            JFrame f = new JFrame(" SELECTED NODES BY DEPTH");
            f.setLayout(new BorderLayout());
            f.setBackground(Color.WHITE);
            f.setPreferredSize(new Dimension(400, 450));
            f.getContentPane().add(new MyMultiNodeAppearanceByDepthLineChart(), BorderLayout.CENTER);
            f.pack();
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    f.setAlwaysOnTop(true);
                }

                @Override public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    f.setAlwaysOnTop(false);
                }
            });
            f.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    MAXIMIZED = false;
                }
            });
            f.setCursor(Cursor.HAND_CURSOR);
            f.setAlwaysOnTop(true);
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
