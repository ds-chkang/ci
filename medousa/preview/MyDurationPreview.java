package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.sequential.category.MySequentialGraphCategory;
import medousa.table.MyTableCellBarChartRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MyDurationPreview
extends JPanel {

    private JTable dataTbl;
    private MyChartPreviewer dataPreviewChart;
    private JComboBox xVariable;
    private JComboBox yVariable;
    private TableRowSorter sorter;
    private JButton showBtn = new JButton("SHOW");
    private JTextField searchTxt;
    private JComboBox searchColumnSelecter;
    private float minValue = 100000000000f;
    private float minFreq = 10000000000f;
    private float maxValue;
    private float maxFreq;
    private float avg;
    private int order = 0;
    private JCheckBox orderBy;
    private JFrame f;
    private JSplitPane contentSplitPane;
    private JComboBox timeOptions;
    private JTextField quantizationTxt;
    private int quantizationValue = 0;
    private int timeScale = 1;
    private int timeOption = 0;
    private JCheckBox chartOrderBy = new JCheckBox("ORDER BY");

    public MyDurationPreview() {}

    public java.util.List<Long> setDuration() {
        java.util.List<Long> durations = new ArrayList<>();
        int row = 0;
        while (row < dataTbl.getRowCount()) {
            String variable1Value = dataTbl.getValueAt(row, xVariable.getSelectedIndex()).toString();
            String variable2Value = dataTbl.getValueAt(row, yVariable.getSelectedIndex()).toString();

            String [] dateTime1 = variable1Value.toString().split(" ");
            String [] dateTime2 = variable2Value.toString().split(" ");

            String completeDateTime1 = "";
            String completeDateTime2 = "";
            if (dateTime1.length == 1) {
                String [] dateElements = dateTime1[0].split("-");
                if (dateElements[0].length() != 4 || dateElements[0].length() != 2 || dateElements[2].length() != 2) {
                    MyMessageUtil.showInfoMsg("Check the date format.");
                    return null;
                }
                completeDateTime1 = (dateTime1[0] + " 00:00:00");
            } else {
                completeDateTime1 = dateTime1[0] + " " + dateTime1[1];
            }

            if (dateTime2.length == 1) {
                String [] dateElements = dateTime2[0].split("-");
                if (dateElements[0].length() != 4 || dateElements[0].length() != 2 || dateElements[2].length() != 2) {
                    MyMessageUtil.showInfoMsg("Check the date format.");
                    return null;
                }
                completeDateTime2 = (dateTime2[0] + " 00:00:00");
            } else {
                completeDateTime2 = dateTime2[0] + " " + dateTime2[1];
            }

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDateTime localDateTime1 = LocalDateTime.parse(completeDateTime1, inputFormatter);
            completeDateTime1 = localDateTime1.format(outputFormatter);
            localDateTime1 = LocalDateTime.parse(completeDateTime1, inputFormatter);

            LocalDateTime localDateTime2 = LocalDateTime.parse(completeDateTime2, inputFormatter);
            completeDateTime2 = localDateTime2.format(outputFormatter);
            localDateTime2 = LocalDateTime.parse(completeDateTime2, inputFormatter);

            Duration duration = Duration.between(localDateTime1, localDateTime2);
            long seconds = Math.abs(duration.toSeconds());

            durations.add(seconds);
            row++;
        }
        return durations;
    }

    private void setContent(List<Long> durations, List<Integer> quantizations) {
        try {
            LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();
            LinkedHashMap<Long, Long> quantizationValueMap = new LinkedHashMap<>();
            long total = 0L;

            maxValue = 0;
            maxFreq = 0;
            minValue = 100000000000L;
            minFreq = 100000000000L;
            avg = 0;

            for (long duration : durations) {
                long scaledDuration = (long) ((float) duration / timeScale);
                if (quantizationValueMap.containsKey(scaledDuration)) {
                    quantizationValueMap.put(scaledDuration, quantizationValueMap.get(scaledDuration) + 1);
                } else {
                    quantizationValueMap.put(scaledDuration, 1L);
                }

                if (quantizations != null && quantizations.size() > 0) {
                    for (long quantization : quantizations) {
                        if (scaledDuration <= quantization) {
                            if (valueMap.containsKey(quantization)) {
                                valueMap.put(quantization, valueMap.get(quantization) + 1);
                            } else {
                                valueMap.put(quantization, 1L);
                            }

                            total += scaledDuration;

                            if (maxValue < scaledDuration) {
                                maxValue = scaledDuration;
                            }

                            if (maxFreq < valueMap.get(quantization)) {
                                maxFreq = valueMap.get(quantization);
                            }

                            if (minValue > scaledDuration) {
                                minValue = scaledDuration;
                            }

                            if (minFreq > valueMap.get(quantization)) {
                                minFreq = valueMap.get(quantization);
                            }
                            break;
                        }
                    }
                } else {
                    total += scaledDuration;

                    if (valueMap.containsKey(scaledDuration)) {
                        valueMap.put(scaledDuration, valueMap.get(scaledDuration) + 1);
                    } else {
                        valueMap.put(scaledDuration, 1L);
                    }

                    if (maxValue < scaledDuration) {
                        maxValue = scaledDuration;
                    }

                    if (maxFreq < valueMap.get(scaledDuration)) {
                        maxFreq = valueMap.get(scaledDuration);
                    }

                    if (minValue > scaledDuration) {
                        minValue = scaledDuration;
                    }

                    if (minFreq > valueMap.get(scaledDuration)) {
                        minFreq = valueMap.get(scaledDuration);
                    }
                }
            }

            if (order == 1) {
                valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
            }
            avg = (float) total / durations.size();

            String[] propertyColumns = {"PROPERTY", "VALUE"};
            String[][] propertyData = {};

            DefaultTableModel propertyModel = new DefaultTableModel(propertyData, propertyColumns);
            JTable propertyTable = new JTable(propertyModel);
            propertyTable.setRowHeight(24);
            propertyTable.setFont(MyDirectGraphVars.f_pln_12);
            propertyTable.setBackground(Color.WHITE);
            propertyTable.setFocusable(false);
            propertyTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            propertyTable.getTableHeader().setOpaque(false);
            propertyTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            propertyModel.addRow(new String[]{"MAX.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) maxValue)});
            propertyModel.addRow(new String[]{"MIN.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) minValue)});
            propertyModel.addRow(new String[]{"AVG.", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat(Float.parseFloat(String.valueOf(avg))))});
            propertyModel.addRow(new String[]{"STD.", MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.threeDecimalFormat((quantizationValueMap.size() > 0 ? getStandardDeviation(quantizationValueMap, avg) : getStandardDeviation(valueMap, avg))))});

            String[] columns = {"NO.", "VALUE", "FREQ.", "MAX. R.", "R."};
            String[][] data = {};

            DefaultTableModel model = new DefaultTableModel(data, columns);
            JTable distributionTable = new JTable(model);
            distributionTable.setRowHeight(24);
            distributionTable.setFont(MyDirectGraphVars.f_pln_12);
            distributionTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(55);
            distributionTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(120);
            distributionTable.getTableHeader().getColumnModel().getColumn(2).setPreferredWidth(75);
            distributionTable.getTableHeader().getColumnModel().getColumn(3).setPreferredWidth(80);
            distributionTable.getTableHeader().getColumnModel().getColumn(4).setPreferredWidth(80);
            distributionTable.setBackground(Color.WHITE);
            distributionTable.setFocusable(false);
            distributionTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
            distributionTable.getTableHeader().setOpaque(false);
            distributionTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
            distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellBarChartRenderer());
            sorter = new TableRowSorter<>(distributionTable.getModel());
            distributionTable.setRowSorter(sorter);

            int i = 0;
            for (long key : valueMap.keySet()) {
                model.addRow(new String[]{"" + (++i),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(key),
                        MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key)),
                        MyDirectGraphMathUtil.threeDecimalFormat((double) valueMap.get(key) / maxFreq),
                        MyDirectGraphMathUtil.threeDecimalPercent((double) valueMap.get(key) / durations.size())
                });
            }

            searchColumnSelecter = new JComboBox();
            searchColumnSelecter.addItem("");
            for (i = 1; i < columns.length; i++) {
                searchColumnSelecter.addItem(columns[i]);
            }
            searchColumnSelecter.setBackground(Color.WHITE);
            searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont11);
            searchColumnSelecter.setFocusable(false);

            JButton searchBtn = new JButton("SEARCH");
            searchBtn.setFocusable(false);

            searchTxt = new JTextField();
            searchTxt.setFont(MyDirectGraphVars.f_pln_12);
            searchTxt.setBorder(BorderFactory.createEtchedBorder());
            searchTxt.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char keyChar = e.getKeyChar();
                    e.setKeyChar(Character.toUpperCase(keyChar));
                }
            });

            JPanel dataSearchPanel = new JPanel();
            dataSearchPanel.setLayout(new BorderLayout(1, 1));
            dataSearchPanel.add(searchTxt, BorderLayout.CENTER);
            dataSearchPanel.add(searchBtn, BorderLayout.EAST);
            searchBtn.addActionListener(this::searchButtonActionPerformed);

            JPanel distributionTableDataSearchPanel = new JPanel();
            distributionTableDataSearchPanel.setLayout(new BorderLayout(1, 1));
            distributionTableDataSearchPanel.add(new JScrollPane(distributionTable), BorderLayout.CENTER);
            distributionTableDataSearchPanel.add(dataSearchPanel, BorderLayout.SOUTH);

            JSplitPane tableSplitPane = new JSplitPane();
            tableSplitPane.setDividerLocation(0.16);
            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            tableSplitPane.setBottomComponent(distributionTableDataSearchPanel);
            tableSplitPane.setTopComponent(new JScrollPane(propertyTable));
            tableSplitPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    tableSplitPane.setDividerLocation(0.16);
                }
            });

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Long key : valueMap.keySet()) {
                dataset.addValue(valueMap.get(key), "", key);
            }

            JFreeChart chart = ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 0));
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_10);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(new Font("Arial", Font.PLAIN, 0));

            CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
            barRenderer.setSeriesPaint(0, Color.decode("#489EE0"));//new Color(0, 0, 0, 0.3f));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont10);

            chartPanel.getChart().removeLegend();

            chartOrderBy = new JCheckBox("ORDER BY");
            chartOrderBy.setFont(MyDirectGraphVars.tahomaPlainFont12);
            chartOrderBy.setFocusable(false);
            if (order == 0) {
                chartOrderBy.setSelected(false);
            } else {
                chartOrderBy.setSelected(true);
            }
            chartOrderBy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateChart();
                        }
                    }).start();
                }
            });

            timeOptions = new JComboBox();
            timeOptions.setFont(MyDirectGraphVars.tahomaPlainFont12);
            timeOptions.setFocusable(false);
            timeOptions.setBackground(Color.WHITE);
            timeOptions.addItem("SEC.");
            timeOptions.addItem("MIN.");
            timeOptions.addItem("HR.");
            timeOptions.addItem("DAY");
            timeOptions.setSelectedIndex(timeOption);
            timeOptions.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            updateChart();
                        }
                    }).start();
                }
            });

            JLabel quantizationLabel = new JLabel("QUANTIZATIONS: ");
            quantizationLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            quantizationTxt = new JTextField();
            if (quantizationValue > 0) {
                quantizationTxt.setText(MyDirectGraphMathUtil.getCommaSeperatedNumber(quantizationValue));
            } else {
                quantizationTxt.setText("");
            }
            quantizationTxt.setHorizontalAlignment(JTextField.CENTER);
            quantizationTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);
            quantizationTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            quantizationTxt.setPreferredSize(new Dimension(70, 24));

            JLabel emptyLabel1 = new JLabel("  ");
            emptyLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel2 = new JLabel("  ");
            emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel3 = new JLabel("  ");
            emptyLabel3.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel chartTopRightPanel = new JPanel();
            chartTopRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 1));
            chartTopRightPanel.add(quantizationLabel);
            chartTopRightPanel.add(quantizationTxt);
            chartTopRightPanel.add(emptyLabel2);
            chartTopRightPanel.add(timeOptions);
            chartTopRightPanel.add(emptyLabel1);
            chartTopRightPanel.add(chartOrderBy);
            chartTopRightPanel.add(emptyLabel3);

            JPanel chartContentPanel = new JPanel();
            chartContentPanel.setLayout(new BorderLayout(1, 1));
            chartContentPanel.add(chartTopRightPanel, BorderLayout.NORTH);
            chartContentPanel.add(chartPanel);

            if (contentSplitPane == null) {
                contentSplitPane = new JSplitPane();
                contentSplitPane.setDividerLocation(0.17);
                contentSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentSplitPane.setDividerSize(10);
                contentSplitPane.setLeftComponent(tableSplitPane);
                contentSplitPane.setRightComponent(chartContentPanel);
                contentSplitPane.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        contentSplitPane.setDividerLocation(0.17);
                        contentSplitPane.setDividerSize(10);

                    }
                });
            } else {
                contentSplitPane = new JSplitPane();
                contentSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
                contentSplitPane.setDividerLocation((int) (f.getWidth() * 0.17));
                contentSplitPane.setLeftComponent(tableSplitPane);
                contentSplitPane.setRightComponent(chartContentPanel);
                contentSplitPane.setDividerSize(10);
                contentSplitPane.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        super.componentResized(e);
                        contentSplitPane.setDividerLocation((int) (f.getWidth() * 0.17));
                    }
                });
            }

            if (f == null) {
                f = new JFrame("DURATION PREVIEW");
                f.setLayout(new BorderLayout(1, 1));
                f.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        f = null;
                        timeOption = 0;
                        timeScale = 1;
                        order = 0;
                        orderBy.setSelected(false);
                        quantizationTxt.setText("");
                        quantizationValue = 0;
                        contentSplitPane = null;
                    }
                });
                f.getContentPane().add(contentSplitPane, BorderLayout.CENTER);
                f.pack();
                f.setVisible(true);
            } else {
                f.getContentPane().add(contentSplitPane, BorderLayout.CENTER);
                f.revalidate();
                f.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private java.util.List<Integer> setQuantizations(java.util.List<Long> durations) {
        if (quantizationTxt.getText().length() > 0 &&
            !quantizationTxt.getText().matches("-?\\d+(\\.\\d+)?")) {
            quantizationValue = 0;
            quantizationTxt.setText("");
            MyMessageUtil.showInfoMsg("Check the number of quantizations");
            return null;
        } else if (quantizationTxt.getText().length() > 0) {
            quantizationValue = Integer.parseInt(quantizationTxt.getText());
            long max = 0;
            long min = 10000000000000L;
            for (long duration : durations) {
                if (duration > max) {
                    max = duration;
                }

                if (duration < min) {
                    min = duration;
                }
            }

            int categories = Integer.parseInt(quantizationTxt.getText());
            MySequentialGraphCategory quantizer = new MySequentialGraphCategory(min, max, categories);
            quantizer.setCategoryIntervals(durations);
            java.util.List<Integer> quantizations = quantizer.getCategoryIntervals();
            return quantizations;
        } else {
            quantizationValue = 0;
            quantizationTxt.setText("");
            return null;
        }
    }

    private void updateChart() {
        MyProgressBar pb = new MyProgressBar(false);

        setChartOrderBy();
        setTimeScaler();

        java.util.List<Long> durations = setDuration();
        if (durations != null) {
            java.util.List<Integer> quantizations = setQuantizations(durations);
            contentSplitPane.removeAll();
            f.remove(contentSplitPane);
            contentSplitPane = null;
            setContent(durations, quantizations);
            f.revalidate();
            f.repaint();
        }

        pb.updateValue(100, 100);
        pb.dispose();
    }

    private void setChartOrderBy() {
        if (chartOrderBy.isSelected()) {
            order = 1;
        } else {
            order = 0;
        }
    }

    private void setTimeScaler() {
        if (timeOptions.getSelectedIndex() == 0) {
            timeScale = 1;
            timeOption = 0;
        } else if (timeOptions.getSelectedIndex() == 1) {
            timeScale = 60;
            timeOption = 1;
        } else if (timeOptions.getSelectedIndex() == 2) {
            timeScale = 60 * 60;
            timeOption = 2;
        } else if (timeOptions.getSelectedIndex() == 3) {
            timeScale = 24 * 3600;
            timeOption = 3;
        }
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        try {
            String searchValue = searchTxt.getText();
            searchTable(searchValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void searchTable(String searchValue) {
        try {
            if (this.searchColumnSelecter.getSelectedIndex() == 0) {
                RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
                this.sorter.setRowFilter(rowFilter);
            } else {
                RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue, this.searchColumnSelecter.getSelectedIndex());
                this.sorter.setRowFilter(rowFilter);
            }
        } catch (Exception ex) {

        }
    }

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(1,1));

            if (dataTbl == null) {dataTbl = dataTable;}
            if (this.dataPreviewChart == null) {this.dataPreviewChart = dataPreviewChart;}

            orderBy = new JCheckBox("ORDER BY");
            orderBy.setFont(MyDirectGraphVars.tahomaPlainFont12);
            orderBy.setFocusable(false);

            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyProgressBar pb = new MyProgressBar(false);
                            java.util.List<Long> durations = setDuration();
                            pb.updateValue(50, 100);
                            if (durations != null) {
                                setContent(durations, new ArrayList<>());
                            }
                            pb.updateValue(100, 100);
                            pb.dispose();
                        }
                    }).start();
                }
            });

            xVariable = new JComboBox();
            xVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                xVariable.addItem(columns[i]);
            }
            xVariable.setBackground(Color.WHITE);
            xVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            xVariable.setFocusable(false);
            xVariable.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {

                        }
                    }).start();
                }
            });

            yVariable = new JComboBox();
            yVariable.addItem("");
            for (int i=1; i < columns.length; i++) {
                yVariable.addItem(columns[i]);
            }
            yVariable.setBackground(Color.WHITE);
            yVariable.setFont(MyDirectGraphVars.tahomaPlainFont11);
            yVariable.setFocusable(false);
            yVariable.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {

                        }
                    }).start();
                }
            });

            JPanel xPanel = new JPanel();
            xPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            JLabel variableLabel1 = new JLabel("X:");
            variableLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);
            xPanel.add(variableLabel1);
            xPanel.add(xVariable);

            JPanel yPanel = new JPanel();
            yPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            JLabel variableLabel2 = new JLabel("Y:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);
            yPanel.add(variableLabel2);
            yPanel.add(yVariable);

            JLabel emptyLabel = new JLabel("       ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(xPanel);
            controlPanel.add(emptyLabel);
            controlPanel.add(yPanel);
            controlPanel.add(orderBy);
            controlPanel.add(showBtn);

            TitledBorder border = BorderFactory.createTitledBorder("DURATION PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    public float getStandardDeviation(LinkedHashMap<Long, Long> valueMap, float avg) {
        try {
            int data = 0;
            float sum = 0f;
            for (long n : valueMap.keySet()) {
                for (int i=0; i < n * valueMap.get(n); i++) {
                    sum += Math.pow(n - avg, 2);
                    data++;
                }
            }
            return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / data));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
