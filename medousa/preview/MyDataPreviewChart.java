package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;

public class MyDataPreviewChart
extends JPanel {

    private int selectedChart = 0;
    private int numberOfTopValues = 100;
    private float numericFromValue = 0f;
    private float numericToValue = 0f;
    private String fromStringValue;
    private String toStringValue;
    private JTable dataTbl;
    private JTable columnDataDistributionTbl;
    private JTextField topValueTxt;
    private JTextField fromValueTxt;
    private JTextField toValueTxt;
    private String [] columns;
    private JLabel fromValueLabel = new JLabel("BETWEEN ");
    private JLabel toValueLabel = new JLabel(" AND ");
    private String avg;
    private String min;
    private String minCategoryValue;
    private String max;
    private String maxCategoryValue;
    private String std;
    private MyColumnPropertyTable inputDataColumnValuePropertyTable = new MyColumnPropertyTable();
    private MyDataPivotPreviewControl pivotDataPreviewControl = new MyDataPivotPreviewControl();
    private MyDataCorrelationPreviewControl correlationPreviewControl = new MyDataCorrelationPreviewControl();
    private MyDataTimeIntervalPreviewControl timeIntervalPreviewControl = new MyDataTimeIntervalPreviewControl();
    private MyColumnQuantizer columnQuantizer = new MyColumnQuantizer();

    public MyDataPreviewChart() {}

    public void decorate(String [] columns, JTable dataTable, JTable columnDataDistributionTable) {
        try {
            removeAll();
            //setBackground(Color.WHITE);
            setLayout(new BorderLayout(3, 3));

            if (this.columns == null) {
                this.columns = columns;
                this.dataTbl = dataTable;
                this.columnDataDistributionTbl = columnDataDistributionTable;
            }

            JLabel topValueLabel = new JLabel("NO. OF TOP VALUES: ");
            topValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            topValueTxt = new JTextField();
            if (numericToValue == 100) {
                topValueTxt.setText("100");
            } else {
                topValueTxt.setText(MyDirectGraphMathUtil.getCommaSeperatedNumber(numberOfTopValues));
            }
            topValueTxt.setHorizontalAlignment(JTextField.CENTER);
            topValueTxt.setPreferredSize(new Dimension(100, 23));
            topValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            topValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            fromValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            toValueTxt = new JTextField();
            toValueTxt.setHorizontalAlignment(JTextField.CENTER);
            toValueTxt.setPreferredSize(new Dimension(100, 23));
            toValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            toValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            toValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            fromValueTxt = new JTextField();
            fromValueTxt.setHorizontalAlignment(JTextField.CENTER);
            fromValueTxt.setPreferredSize(new Dimension(100, 23));
            fromValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            fromValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel topPanel = new JPanel();
            //topPanel.setBackground(Color.WHITE);
            topPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));

            JComboBox columnsOptions = new JComboBox();
            columnsOptions.setBackground(Color.WHITE);
            columnsOptions.setFocusable(false);
            columnsOptions.setFont(MyDirectGraphVars.tahomaPlainFont12);
            for (int i = 1; i < columns.length; i++) {
                columnsOptions.addItem(columns[i]);
            }
            columnsOptions.setSelectedIndex(selectedChart);
            columnsOptions.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            selectedChart = columnsOptions.getSelectedIndex();
                            String topValueContraint = topValueTxt.getText().replaceAll(" ", "").replaceAll(",", "");
                            numberOfTopValues = (topValueContraint.length() == 0 ? 100 : Integer.parseInt(topValueContraint));

                            String fromValueStr = fromValueTxt.getText().trim();
                            String toValueStr = toValueTxt.getText().trim();

                            if (fromValueStr.trim().length() > 0 && toValueStr.trim().length() > 0) {
                                if (fromValueStr.matches("\\d+(\\.\\d+)?") && !toValueStr.matches("\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Check the between values.");
                                    return;
                                } else if (!fromValueStr.matches("\\d+(\\.\\d+)?") && toValueStr.matches("\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Check the between values.");
                                    return;
                                } else if (fromValueStr.matches("\\d+(\\.\\d+)?") && toValueStr.matches("\\d+(\\.\\d+)?")) {
                                    numericFromValue = (fromValueTxt.getText().trim().length() == 0 ? 0f : Float.parseFloat(fromValueTxt.getText().trim()));
                                    numericToValue = (toValueTxt.getText().trim().length() == 0 ? 0f : Float.parseFloat(toValueTxt.getText().trim()));
                                } else {
                                    fromStringValue = fromValueStr.toLowerCase();
                                    toStringValue = toValueStr.toLowerCase();
                                }
                            } else if ((fromValueStr.trim().length() == 0 && toValueStr.trim().length() > 0) ||
                                    (fromValueStr.trim().length() > 0 && toValueStr.trim().length() == 0)) {
                                MyMessageUtil.showInfoMsg("Check the between values.");
                                return;
                            }

                            avg = "";
                            max = "";
                            min = "";
                            std = "";
                            decorate(columns, dataTbl, columnDataDistributionTbl);
                        }
                    }).start();
                }
            });

            JLabel emptyLabel = new JLabel(" ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel2 = new JLabel("  ");
            emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JButton enlargeBtn = new JButton("+");
            enlargeBtn.setFocusable(false);
            enlargeBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            enlargeBtn.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            enlarge();
                        }
                    }).start();
                }
            });

            topPanel.add(fromValueLabel);
            topPanel.add(fromValueTxt);
            topPanel.add(toValueLabel);
            topPanel.add(toValueTxt);
            topPanel.add(emptyLabel2);
            topPanel.add(topValueLabel);
            topPanel.add(topValueTxt);
            topPanel.add(emptyLabel);
            topPanel.add(columnsOptions);
            topPanel.add(enlargeBtn);
            //add(topPanel, BorderLayout.NORTH);

            ChartPanel chartPanel = new ChartPanel(setChart());
            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 0));
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.f_pln_8);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_11);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.f_pln_8);
            //chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelPaint(Color.LIGHT_GRAY);

            CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
            barRenderer.setSeriesPaint(0, Color.decode("#489EE0"));//new Color(0, 0, 0, 0.3f));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont10);

            chartPanel.getChart().removeLegend();
            chartPanel.setBorder(BorderFactory.createLoweredBevelBorder());
            inputDataColumnValuePropertyTable.decorate(
                MyDirectGraphMathUtil.getCommaSeperatedNumber(dataTbl.getRowCount()),
                max,
                maxCategoryValue,
                min,
                minCategoryValue,
                avg,
                std
            );

            JPanel chartTopPanel = new JPanel();
            chartTopPanel.setLayout(new BorderLayout(1,1));
            chartTopPanel.add(topPanel, BorderLayout.NORTH);
            chartTopPanel.add(chartPanel, BorderLayout.CENTER);

            pivotDataPreviewControl.decorate(this.columns, this.dataTbl);
            correlationPreviewControl.decorate(this.columns, this.dataTbl);
            timeIntervalPreviewControl.decorate(this.columns, this.dataTbl);
            columnQuantizer.decorate(this.columns, this.dataTbl);

            JSplitPane columnValuePropertyQuantizationControlSplitpane = new JSplitPane();
            columnValuePropertyQuantizationControlSplitpane.setDividerLocation(0.38);
            columnValuePropertyQuantizationControlSplitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            columnValuePropertyQuantizationControlSplitpane.setTopComponent(inputDataColumnValuePropertyTable);
            columnValuePropertyQuantizationControlSplitpane.setBottomComponent(columnQuantizer);
            columnValuePropertyQuantizationControlSplitpane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    columnValuePropertyQuantizationControlSplitpane.setDividerLocation(0.38);
                }
            });

            JSplitPane pivotCorrelationPreviewControlSplitpane = new JSplitPane();
            pivotCorrelationPreviewControlSplitpane.setDividerLocation(0.58);
            pivotCorrelationPreviewControlSplitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            pivotCorrelationPreviewControlSplitpane.setTopComponent(pivotDataPreviewControl);
            pivotCorrelationPreviewControlSplitpane.setBottomComponent(correlationPreviewControl);
            pivotCorrelationPreviewControlSplitpane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    pivotCorrelationPreviewControlSplitpane.setDividerLocation(0.58);
                }
            });

            JSplitPane propertyDataPreviewControlSplitPane = new JSplitPane();
            propertyDataPreviewControlSplitPane.setDividerLocation(0.48);
            propertyDataPreviewControlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            propertyDataPreviewControlSplitPane.setTopComponent(columnValuePropertyQuantizationControlSplitpane);
            propertyDataPreviewControlSplitPane.setBottomComponent(pivotCorrelationPreviewControlSplitpane);
            propertyDataPreviewControlSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    propertyDataPreviewControlSplitPane.setDividerLocation(0.48);
                }
            });

            JSplitPane chartColumnValueStatisticsSplitPane = new JSplitPane();
            chartColumnValueStatisticsSplitPane.setDividerLocation((int)(0.17*getWidth()));
            chartColumnValueStatisticsSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            chartColumnValueStatisticsSplitPane.setLeftComponent(propertyDataPreviewControlSplitPane);
            chartColumnValueStatisticsSplitPane.setRightComponent(chartTopPanel);
            chartColumnValueStatisticsSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    chartColumnValueStatisticsSplitPane.setDividerLocation((int)(0.17*getWidth()));
                }
            });
            chartColumnValueStatisticsSplitPane.setBorder(BorderFactory.createLoweredSoftBevelBorder());

            add(chartColumnValueStatisticsSplitPane, BorderLayout.CENTER);
            setBorder(BorderFactory.createLoweredBevelBorder());
            revalidate();
            repaint();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private void enlarge() {
        MyDataPreviewChart dataPreviewChart = new MyDataPreviewChart();
        dataPreviewChart.decorate(columns, dataTbl, columnDataDistributionTbl);

        JFrame f = new JFrame();
        f.setLayout(new BorderLayout(3,3));
        f.getContentPane().add(dataPreviewChart, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    private JFreeChart setChart() {
        if (dataTbl.getValueAt(0, selectedChart+1).toString().matches("\\d+(\\.\\d+)?")) {
            return setNumericDataChart();
        } else {
            return setStringDataChart();
        }
    }

    private JFreeChart setStringDataChart() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<String, Long> valueMap = new LinkedHashMap<>();
            int row = dataTbl.getRowCount() - 1;
            while (row > -1) {
                String value = dataTbl.getValueAt(row, selectedChart+1).toString().trim();
                if (valueMap.containsKey(value)) {
                    valueMap.put(value, valueMap.get(value) + 1);
                } else {
                    valueMap.put(value, 1L);
                }
                row--;
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
            pb.updateValue(50, 100);

            int i=0;
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            if (toStringValue != null) {
                boolean fromValueFound = false;
                for (String key : valueMap.keySet()) {
                    if (fromStringValue.equals(key)) {
                        fromValueFound = true;
                        max = key;
                        maxCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                        dataset.addValue(valueMap.get(key), "", key);
                    } else if (toStringValue.equals(key)) {
                        min = key;
                        minCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                        dataset.addValue(valueMap.get(key), "", key);
                        break;
                    } else if (fromValueFound) {
                        dataset.addValue(valueMap.get(key), "", key);
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        if (i == valueMap.size()) {
                            min = key;
                            minCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                        }
                        break;
                    }
                }
            } else {
                for (String key : valueMap.keySet()) {
                    dataset.addValue(valueMap.get(key), "", key);

                    i++;
                    if (i == 1) {
                        max = key;
                        maxCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                    }

                    if (i == valueMap.size()) {
                        min = key;
                        minCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        min = key;
                        minCategoryValue = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key));
                        break;
                    }
                }
            }

            pb.updateValue(80, 100);

            row = columnDataDistributionTbl.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) columnDataDistributionTbl.getModel()).removeRow(row-1);
                row = columnDataDistributionTbl.getRowCount();
            }

            i = 0;
            for (String value : valueMap.keySet()){
                ((DefaultTableModel)columnDataDistributionTbl.getModel()).addRow(new String[]{
                        ("" + (++i)),
                        "" + value,
                        "" + valueMap.get(value)
                });
            }

            float total = 0f;
            for (long value : valueMap.values()) {
                total += value;
            }
            avg = MyDirectGraphMathUtil.threeDecimalFormat(total/valueMap.size());

            columnDataDistributionTbl.revalidate();
            columnDataDistributionTbl.repaint();

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            pb.updateValue(100, 100);
            pb.dispose();
            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            //ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
        return null;
    }

    private JFreeChart setNumericDataChart() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            long total = 0L;
            LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();
            int row = dataTbl.getRowCount() - 1;
            while (row > -1) {
                long value = Long.parseLong(dataTbl.getValueAt(row, selectedChart + 1).toString().trim().split("\\.")[0]);
                if (valueMap.containsKey(value)) {
                    valueMap.put(value, valueMap.get(value) + 1);
                } else {
                    valueMap.put(value, 1L);
                }
                row--;
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
            pb.updateValue(50, 100);

            int i=0;
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            if (numericToValue > 0) {
                for (Long key : valueMap.keySet()) {
                    long valueToCheck = key;
                    if (numericFromValue <= valueToCheck && numericToValue >= valueToCheck) {
                        if (valueToCheck >= numericToValue) {
                            max = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueToCheck);
                        } else if (valueToCheck == numericToValue) {
                            min = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueToCheck);
                        }
                        dataset.addValue(valueMap.get(key), "", key);
                        total += valueMap.get(key);
                        i++;
                        if (numberOfTopValues > 0 && i == numberOfTopValues) {
                            min = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueToCheck);
                            break;
                        }
                    }
                }
            } else {
                for (Long key : valueMap.keySet()) {
                    dataset.addValue(valueMap.get(key), "", key);

                    total += valueMap.get(key);

                    i++;
                    if (i == 1) {
                        max = MyDirectGraphMathUtil.getCommaSeperatedNumber(key);
                    }

                    if (i == valueMap.size()) {
                        min = MyDirectGraphMathUtil.getCommaSeperatedNumber(key);
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        min = MyDirectGraphMathUtil.getCommaSeperatedNumber(key);
                        break;
                    }
                }
            }
            avg = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((float)total/i));
            pb.updateValue(80, 100);

            row = columnDataDistributionTbl.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) columnDataDistributionTbl.getModel()).removeRow(row-1);
                row = columnDataDistributionTbl.getRowCount();
            }

            i = 0;
            for (Long value : valueMap.keySet()){
                ((DefaultTableModel)columnDataDistributionTbl.getModel()).addRow(new String[]{
                    ("" + (++i)),
                     "" + value,
                     "" + valueMap.get(value)
                });
            }

            std = MyDirectGraphMathUtil.twoDecimalFormat(getColumnNumericValueStandardDeviation(valueMap));

            columnDataDistributionTbl.revalidate();
            columnDataDistributionTbl.repaint();

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;
            pb.updateValue(100, 100);
            pb.dispose();

            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {
            //ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
        return null;
    }

    public float getColumnNumericValueStandardDeviation(LinkedHashMap<Long, Long> valueMap) {
        float sum = 0.00f;
        for (long n : valueMap.keySet()) {
            sum += n;
        }
        double mean = sum / valueMap.size();
        sum = 0f;
        for (long n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / valueMap.size()));
    }

}
