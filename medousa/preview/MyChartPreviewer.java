package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.sequential.category.MySequentialGraphCategory;
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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class MyChartPreviewer
extends JPanel {

    private JLabel fromValueLabel = new JLabel("BETWEEN ");
    private JLabel toValueLabel = new JLabel(" AND ");
    private int numberOfTopValues = 150;
    private int selectedChart;
    private float numericFromValue;
    private float numericToValue;
    private String fromStringValue;
    private String toStringValue;
    private JTable dataTbl;
    public String [] columns;
    public JTable columnDistributionTbl;
    private JTextField topValueTxt;
    private JTextField fromValueTxt;
    private JTextField toValueTxt;
    private JTextField quantizationTxt;
    private int quantizationValue;
    private String avg;
    private String min;
    private String minCategoryValue;
    private String max;
    private String maxCategoryValue;
    private String std;

    public MyChartPreviewer() {}

    public void decorate(JTable dataTable, JTable columnDataDistributionTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(1, 1));

            if (this.columns == null) {
                this.columns = MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders();
                this.dataTbl = dataTable;
                this.columnDistributionTbl = columnDataDistributionTable;
            }

            JLabel topValueLabel = new JLabel("TOP V.: ");
            topValueLabel.setToolTipText("NO. OF TOP VALUES");
            topValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);
            topValueTxt = new JTextField();
            topValueTxt.setText(MyDirectGraphMathUtil.getCommaSeperatedNumber(numberOfTopValues));
            topValueTxt.setHorizontalAlignment(JTextField.CENTER);
            topValueTxt.setPreferredSize(new Dimension(70, 23));
            topValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            topValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel quantizationLabel = new JLabel(" Q.: ");
            quantizationLabel.setToolTipText("QUANTIZATION");
            quantizationLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            quantizationTxt = new JTextField();
            quantizationTxt.setText("0");
            quantizationTxt.setText(MyDirectGraphMathUtil.getCommaSeperatedNumber(quantizationValue));
            quantizationTxt.setHorizontalAlignment(JTextField.CENTER);
            quantizationTxt.setPreferredSize(new Dimension(70, 23));
            quantizationTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            quantizationTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel topLeftPanel = new JPanel();
            topLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            topLeftPanel.add(quantizationLabel);
            topLeftPanel.add(quantizationTxt);

            fromValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            toValueTxt = new JTextField();
            toValueTxt.setHorizontalAlignment(JTextField.CENTER);
            toValueTxt.setPreferredSize(new Dimension(70, 23));
            toValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            toValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            toValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            fromValueTxt = new JTextField();
            fromValueTxt.setHorizontalAlignment(JTextField.CENTER);
            fromValueTxt.setPreferredSize(new Dimension(70, 23));
            fromValueTxt.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            fromValueTxt.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BorderLayout( 1, 1));

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
                            numberOfTopValues = (topValueContraint.length() == 0 ? 150 : Integer.parseInt(topValueContraint));
                            if (quantizationTxt.getText().length() > 0 && Integer.parseInt(quantizationTxt.getText()) > 0) {
                                quantizationValue = Integer.parseInt(quantizationTxt.getText());
                            }


                            String fromValueStr = fromValueTxt.getText().trim();
                            String toValueStr = toValueTxt.getText().trim();

                            if (fromValueStr.trim().length() > 0 && toValueStr.trim().length() > 0) {
                                if (fromValueStr.matches("-?\\d+(\\.\\d+)?") && !toValueStr.matches("-?\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Check the between values.");
                                    return;
                                } else if (!fromValueStr.matches("-?\\d+(\\.\\d+)?") && toValueStr.matches("-?\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Check the between values.");
                                    return;
                                } else if (fromValueStr.matches("-?\\d+(\\.\\d+)?") && toValueStr.matches("-?\\d+(\\.\\d+)?")) {
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
                            decorate(dataTbl, columnDistributionTbl);
                        }
                    }).start();
                }
            });

            JLabel emptyLabel = new JLabel(" ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel2 = new JLabel("  ");
            emptyLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel emptyLabel3 = new JLabel("  ");
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

            JPanel topRightPanel = new JPanel();
            topRightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 1,1));
            topRightPanel.add(fromValueLabel);
            topRightPanel.add(fromValueTxt);
            topRightPanel.add(toValueLabel);
            topRightPanel.add(toValueTxt);
            topRightPanel.add(emptyLabel2);
            topRightPanel.add(topValueLabel);
            topRightPanel.add(topValueTxt);
            topRightPanel.add(emptyLabel);
            topRightPanel.add(columnsOptions);
            topRightPanel.add(enlargeBtn);

            topPanel.add(topRightPanel, BorderLayout.CENTER);
            topPanel.add(topLeftPanel, BorderLayout.WEST);

            ChartPanel chartPanel = new ChartPanel(setChart());
            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(new Font("Arial", Font.PLAIN, 0));
            chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.f_pln_8);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_11);
            chartPanel.getChart().getCategoryPlot().getRangeAxis().setLabelFont(MyDirectGraphVars.f_pln_8);

            CategoryAxis domainAxis = chartPanel.getChart().getCategoryPlot().getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);

            BarRenderer barRenderer = (BarRenderer) chartPanel.getChart().getCategoryPlot().getRenderer();
            barRenderer.setSeriesPaint(0, Color.decode("#489EE0"));//new Color(0, 0, 0, 0.3f));
            barRenderer.setShadowPaint(Color.WHITE);
            barRenderer.setBaseFillPaint(Color.decode("#07CF61"));
            barRenderer.setBarPainter(new StandardBarPainter());
            barRenderer.setBaseLegendTextFont(MyDirectGraphVars.tahomaPlainFont10);

            chartPanel.getChart().removeLegend();
            //chartPanel.setBorder(BorderFactory.createLoweredBevelBorder());

            JPanel statPanel = new JPanel();
            statPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

            JLabel maxLabel = new JLabel("MAX.: " + (std == null ? maxCategoryValue : max) + "     ");
            maxLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JLabel minLabel = new JLabel("MIN.: " + (std == null ? minCategoryValue : min) + "    ");
            minLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JLabel avgLabel = new JLabel("AVG.: " + avg + "    ");
            avgLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);

            statPanel.add(maxLabel);
            statPanel.add(minLabel);
            statPanel.add(avgLabel);

            if (std != null && !std.equals("")) {
                JLabel stdLabel = new JLabel("STD.: " + std);
                stdLabel.setFont(MyDirectGraphVars.tahomaBoldFont12);
                statPanel.add(stdLabel);
            }

            JPanel chart = new JPanel();
            chart.setLayout(new BorderLayout(1,1));
            chart.add(topPanel, BorderLayout.NORTH);
            chart.add(chartPanel, BorderLayout.CENTER);
            chart.add(statPanel, BorderLayout.SOUTH);
            //chart.setBorder(BorderFactory.createLoweredSoftBevelBorder());

            add(chart, BorderLayout.CENTER);
            //setBorder(BorderFactory.createLoweredBevelBorder());
            revalidate();
            repaint();
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private void enlarge() {
        MyChartPreviewer dataPreviewChart = new MyChartPreviewer();
        dataPreviewChart.decorate(dataTbl, columnDistributionTbl);
        JFrame f = new JFrame();
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(dataPreviewChart, BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }

    private JFreeChart setChart() {
        int row = dataTbl.getRowCount();
        boolean isNumericColumn = true;
        for (; row > 0; row--) {
            String value = dataTbl.getValueAt(row-1, selectedChart + 1).toString().split("\\.")[0];
            if (!value.matches("-?\\d+(\\.\\d+)?")) {
                isNumericColumn = false;
                break;
            }
        }

        if (isNumericColumn) {
            return setNumericDataChart();
        } else {
            if (quantizationTxt.getText().length() > 0 && Integer.parseInt(quantizationTxt.getText()) > 0) {
                quantizationTxt.setText("0");
            }
            return setStringDataChart();
        }
    }

    private JFreeChart setStringDataChart() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            int totalRows = dataTbl.getRowCount();
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
                        maxCategoryValue = "" + valueMap.get(key);
                        dataset.addValue(valueMap.get(key), "", key);
                    } else if (toStringValue.equals(key)) {
                        min = key;
                        minCategoryValue = "" + valueMap.get(key);
                        dataset.addValue(valueMap.get(key), "", key);
                        break;
                    } else if (fromValueFound) {
                        dataset.addValue(valueMap.get(key), "", key);
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        if (i == valueMap.size()) {
                            min = key;
                            minCategoryValue = "" + valueMap.get(key);
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
                        maxCategoryValue = "" + valueMap.get(key);
                    }

                    if (i == valueMap.size()) {
                        min = key;
                        minCategoryValue = "" + valueMap.get(key);
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        min = key;
                        minCategoryValue = "" + valueMap.get(key);
                        break;
                    }
                }
            }

            pb.updateValue(80, 100);

            row = columnDistributionTbl.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) columnDistributionTbl.getModel()).removeRow(row-1);
                row = columnDistributionTbl.getRowCount();
            }

            i = 0;
            for (String value : valueMap.keySet()){
                ((DefaultTableModel) columnDistributionTbl.getModel()).addRow(new String[]{
                   ("" + (++i)),
                    "" + value,
                    "" + valueMap.get(value),
                    MyDirectGraphMathUtil.threeDecimalFormat((double)valueMap.get(value)/Long.parseLong(maxCategoryValue)),
                    MyDirectGraphMathUtil.threeDecimalPercent((double) valueMap.get(value)/totalRows)
                });
            }

            float total = 0f;
            for (long value : valueMap.values()) {
                total += value;
            }
            avg = MyDirectGraphMathUtil.threeDecimalFormat(total/valueMap.size());

            columnDistributionTbl.revalidate();
            columnDistributionTbl.repaint();

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

    private java.util.List<Integer> getQutizations() {
        java.util.List<Long> columnsValues = new ArrayList<>();
        int row = dataTbl.getRowCount();
        long max = 0;
        long min = 100000000000L;
        while (row > 0) {
            long value = (long) Float.parseFloat(dataTbl.getValueAt(row-1, selectedChart + 1).toString());
            if (max < value) {
                max = value;
            }
            if (min > value) {
                min = value;
            }
            columnsValues.add(value);
            row--;
        }


        int categories = Integer.parseInt(quantizationTxt.getText());
        MySequentialGraphCategory quantizer = new MySequentialGraphCategory(min, max, categories);
        quantizer.setCategoryIntervals(columnsValues);
        java.util.List<Integer> quantizations = quantizer.getCategoryIntervals();
        return quantizations;
    }

    private JFreeChart setNumericDataChart() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();
            long total = 0L;
            int totalRows = dataTbl.getRowCount();
            if (quantizationTxt.getText().length() > 0 && Integer.parseInt(quantizationTxt.getText()) > 0) {
                java.util.List<Integer> quantizations = getQutizations();
                int row = dataTbl.getRowCount() - 1;
                while (row > -1) {
                    long value = Long.parseLong(dataTbl.getValueAt(row, selectedChart + 1).toString().trim().split("\\.")[0]);
                    for (int quantization : quantizations) {
                        if (value < quantization) {
                            if (valueMap.containsKey((long) quantization)) {
                                valueMap.put((long) quantization, valueMap.get((long) quantization) + 1);
                            } else {
                                valueMap.put((long) quantization, 1L);
                            }
                            break;
                        }
                    }
                    row--;
                }
            } else {
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
            }
            valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
            pb.updateValue(50, 100);

            int i=0;
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            if (numericToValue > 0) {
                for (Long key : valueMap.keySet()) {
                    if (numericFromValue <= key && numericToValue >= key) {
                        if (key >= numericToValue) {
                            max = String.valueOf(valueMap.get(key));
                        } else if (key == numericToValue) {
                            min = String.valueOf(valueMap.get(key));
                        }
                        dataset.addValue(valueMap.get(key), "", key);
                        total += valueMap.get(key);
                        i++;
                        if (numberOfTopValues > 0 && i == numberOfTopValues) {
                            min = String.valueOf(valueMap.get(key));
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
                        max = String.valueOf(valueMap.get(key));
                    }

                    if (i == valueMap.size()) {
                        min = String.valueOf(valueMap.get(key));
                    }

                    if (numberOfTopValues > 0 && i == numberOfTopValues) {
                        min = String.valueOf(valueMap.get(key));
                        break;
                    }
                }
            }
            avg = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((float)total/i));
            pb.updateValue(80, 100);

            int row = columnDistributionTbl.getRowCount();
            while (row > 0) {
                ((DefaultTableModel) columnDistributionTbl.getModel()).removeRow(row-1);
                row = columnDistributionTbl.getRowCount();
            }

            i = 0;
            for (Long value : valueMap.keySet()){
                ((DefaultTableModel) columnDistributionTbl.getModel()).addRow(new String[]{
                    ("" + (++i)),
                     "" + value,
                     "" + valueMap.get(value),
                     MyDirectGraphMathUtil.threeDecimalFormat((double) valueMap.get(value)/Double.parseDouble(max)),
                     MyDirectGraphMathUtil.threeDecimalPercent((double) valueMap.get(value)/totalRows)
                });
            }

            std = MyDirectGraphMathUtil.threeDecimalFormat(getColumnNumericValueStandardDeviation(valueMap, Float.parseFloat(avg)));

            columnDistributionTbl.revalidate();
            columnDistributionTbl.repaint();

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

    public float getColumnNumericValueStandardDeviation(LinkedHashMap<Long, Long> valueMap, float avg) {
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
