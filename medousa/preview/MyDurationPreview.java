package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.table.MyTableCellRenderer;
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
    private JLabel result = new JLabel();
    private float minValue = 100000000000f;
    private float minFreq = 10000000000f;
    private float maxValue;
    private float maxFreq;
    private float avg;
    private float std;

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

    private void setContent(List<Long> durations) {
        LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();

        long total = 0L;
        for (long duration : durations) {
            if (valueMap.containsKey(duration)) {
                 valueMap.put(duration, valueMap.get(duration) + 1);
            } else {
                valueMap.put(duration, 1L);
            }

            total += duration;

            if (maxValue < duration) {
                maxValue = duration;
            }

            if (maxFreq < valueMap.get(duration)) {
                maxFreq = valueMap.get(duration);
            }

            if (minValue > duration) {
                minValue = duration;
            }

            if (minFreq > valueMap.get(duration)) {
                minFreq = valueMap.get(duration);
            }
        }
        valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
        avg = (float) total/durations.size();

        String [] propertyColumns = {"PROPERTY", "VALUE"};
        String [][] propertyData = {};

        DefaultTableModel propertyModel = new DefaultTableModel(propertyData, propertyColumns);
        JTable propertyTable = new JTable(propertyModel);
        propertyTable.setRowHeight(24);
        propertyTable.setFont(MyDirectGraphVars.f_pln_12);
        propertyTable.setBackground(Color.WHITE);
        propertyTable.setFocusable(false);
        propertyTable.getTableHeader().setBackground(new Color(0,0,0,0));
        propertyTable.getTableHeader().setOpaque(false);
        propertyTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        propertyModel.addRow(new String[]{"MAX.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) maxValue)});
        propertyModel.addRow(new String[]{"MIN.", MyDirectGraphMathUtil.getCommaSeperatedNumber((long) minValue)});
        propertyModel.addRow(new String[]{"AVG.", MyDirectGraphMathUtil.threeDecimalFormat(Float.parseFloat(String.valueOf(avg)))});
        propertyModel.addRow(new String[]{"STD.", MyDirectGraphMathUtil.threeDecimalFormat(getStandardDeviation(valueMap))});

        String [] columns = {"NO.", "VALUE", "FREQ.", "MAX. R.", "R."};
        String [][] data = {};

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
        distributionTable.getTableHeader().setBackground(new Color(0,0,0,0));
        distributionTable.getTableHeader().setOpaque(false);
        distributionTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellRenderer());
        sorter = new TableRowSorter<>(distributionTable.getModel());
        distributionTable.setRowSorter(sorter);

        int i = 0;
        for (long key : valueMap.keySet()) {
            model.addRow(new String[]{"" + (++i),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(key),
                    MyDirectGraphMathUtil.getCommaSeperatedNumber(valueMap.get(key)),
                    MyDirectGraphMathUtil.threeDecimalFormat((double) valueMap.get(key)/maxFreq),
                    MyDirectGraphMathUtil.threeDecimalPercent((double) valueMap.get(key)/durations.size())
            });
        }

        searchColumnSelecter = new JComboBox();
        searchColumnSelecter.addItem("");
        for (i=1; i < columns.length; i++) {
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
        dataSearchPanel.setLayout(new BorderLayout(1,1));
        dataSearchPanel.add(searchTxt, BorderLayout.CENTER);
        dataSearchPanel.add(searchBtn, BorderLayout.EAST);
        searchBtn.addActionListener(this::searchButtonActionPerformed);

        JPanel distributionTableDataSearchPanel = new JPanel();
        distributionTableDataSearchPanel.setLayout(new BorderLayout(1,1));
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

        JSplitPane contentSplitPane = new JSplitPane();
        contentSplitPane.setDividerLocation(0.2);
        contentSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        contentSplitPane.setLeftComponent(tableSplitPane);
        contentSplitPane.setRightComponent(chartPanel);
        contentSplitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                contentSplitPane.setDividerLocation(0.2);
            }
        });

        JFrame f = new JFrame("DURATION PREVIEW");
        f.setLayout(new BorderLayout(1,1));
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.getContentPane().add(contentSplitPane);
        f.pack();
        f.setVisible(true);
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
                                setContent(durations);
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

            result.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(xPanel);
            controlPanel.add(emptyLabel);
            controlPanel.add(yPanel);
            controlPanel.add(showBtn);
            controlPanel.add(result);

            TitledBorder border = BorderFactory.createTitledBorder("DURATION PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    public float getStandardDeviation(LinkedHashMap<Long, Long> valueMap) {
        float sum = 0.00f;
        int data = 0;
        for (long n : valueMap.keySet()) {
            for (int i=0; i < n*valueMap.get(n); i++) {
                sum += n;
                data++;
            }
        }
        double mean = sum / data;
        sum = 0f;
        for (long n : valueMap.keySet()) {
            sum += Math.pow(n - mean, 2);
        }
        return (sum == 0.00f ? 0.00f : (float) Math.sqrt(sum / data));
    }
}
