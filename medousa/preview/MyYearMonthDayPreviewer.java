package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

public class MyYearMonthDayPreviewer
extends JPanel {

    private JTable dataTbl;
    private MyChartPreviewer dataPreviewChart;
    private JComboBox xVariable;
    private JComboBox values;
    private JComboBox pivot;
    private JLabel result = new JLabel();
    private JComboBox searchColumnSelecter;
    private long minValue = 1000000000000L;
    private long minFreq = 1000000000000L;
    private long maxValue;
    private long maxFreq;
    private float avg;
    private float std;
    private JCheckBox orderBy;


    private JButton showBtn = new JButton("SHOW");
    private JTextField searchTxt = new JTextField();
    private TableRowSorter sorter;

    public MyYearMonthDayPreviewer() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {

            }
        });
    }

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(3,3));
            final MyYearMonthDayPreviewer timeDurationPreviewer = this;

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

                            LinkedHashMap<String, Long> extractedDateData = new LinkedHashMap<>();
                            int pbCnt = 0;
                            int totalRows = dataTable.getRowCount();
                            int row = dataTbl.getRowCount();
                            while (row > 0) {
                                String [] dateTime = dataTbl.getValueAt(row-1, xVariable.getSelectedIndex()).toString().split(" ");
                                String completeDateTime = "";

                                if (dateTime.length == 1) {
                                    String [] dateElements = dateTime[0].split("-");
                                    if (dateElements[0].length() != 4 || dateElements[0].length() != 2 || dateElements[2].length() != 2) {
                                        MyMessageUtil.showInfoMsg("Check the date format.");
                                        return;
                                    }
                                    completeDateTime = (dateTime[0] + " 00:00:00");
                                } else {
                                    completeDateTime = dateTime[0] + " " + dateTime[1];
                                }

                                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm:ss");
                                LocalDateTime localDateTime = LocalDateTime.parse(completeDateTime, inputFormatter);

                                String [] dateElements = completeDateTime.split(" ")[0].split("-");

                                if (values.getSelectedItem().equals("YEAR")) {
                                    if (extractedDateData.containsKey(dateElements[0])) {
                                        extractedDateData.put(dateElements[0], extractedDateData.get(dateElements[0])+1);
                                    } else {
                                        extractedDateData.put(dateElements[0], 1L);
                                    }
                                } else if (values.getSelectedItem().equals("MONTH")) {
                                    if (extractedDateData.containsKey(dateElements[1])) {
                                        extractedDateData.put(dateElements[1], extractedDateData.get(dateElements[1])+1);
                                    } else {
                                        extractedDateData.put(dateElements[1], 1L);
                                    }
                                } else if (values.getSelectedItem().equals("DAY")) {
                                    if (extractedDateData.containsKey(dateElements[2])) {
                                        extractedDateData.put(dateElements[2], extractedDateData.get(dateElements[2])+1);
                                    } else {
                                        extractedDateData.put(dateElements[2], 1L);
                                    }
                                } else if (values.getSelectedItem().equals("DAY OF WEEK")) {
                                    String day = localDateTime.getDayOfWeek().toString();
                                    if (extractedDateData.containsKey(day)) {
                                        extractedDateData.put(day, extractedDateData.get(day)+1);
                                    } else {
                                        extractedDateData.put(day, 1L);
                                    }
                                }

                                row--;
                                pb.updateValue(++pbCnt, totalRows);
                            }

                            searchColumnSelecter = new JComboBox();
                            searchColumnSelecter.setFont(MyDirectGraphVars.tahomaPlainFont12);
                            searchColumnSelecter.setFocusable(false);
                            searchColumnSelecter.setBackground(Color.WHITE);
                            searchColumnSelecter.addItem("");
                            for (int i = 1; i < columns.length; i++) {
                                searchColumnSelecter.addItem(columns[i]);
                            }

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
                            distributionTable.getColumnModel().getColumn(3).setCellRenderer(new MyTableCellBarChartRenderer());
                            sorter = new TableRowSorter<>(distributionTable.getModel());
                            distributionTable.setRowSorter(sorter);

                            JButton searchBtn = new JButton("SEARCH");
                            searchBtn.setFocusable(false);
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
                            dataSearchPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
                            searchBtn.addActionListener(timeDurationPreviewer::searchButtonActionPerformed);

                            JPanel dataTablePanel = new JPanel();
                            dataTablePanel.setLayout(new BorderLayout(1,1));
                            dataTablePanel.add(new JScrollPane(distributionTable), BorderLayout.CENTER);
                            dataTablePanel.add(dataSearchPanel, BorderLayout.SOUTH);

                            JSplitPane tableSplitPane = new JSplitPane();
                            tableSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                            tableSplitPane.setTopComponent(new JScrollPane(propertyTable));
                            tableSplitPane.setBottomComponent(dataTablePanel);
                            tableSplitPane.setOneTouchExpandable(false);
                            tableSplitPane.setDividerLocation(0.17);
                            tableSplitPane.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentResized(ComponentEvent e) {
                                    super.componentResized(e);
                                    tableSplitPane.setDividerLocation(0.17);
                                }
                            });

                            ChartPanel chartPanel = new ChartPanel(setChart(extractedDateData, distributionTable, propertyTable));
                            chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                            chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                            chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);
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

                            JSplitPane splitPane = new JSplitPane();
                            splitPane.setLeftComponent(tableSplitPane);
                            splitPane.setRightComponent(chartPanel);
                            splitPane.setDividerLocation(0.20);
                            splitPane.addComponentListener(new ComponentAdapter() {
                                @Override
                                public void componentResized(ComponentEvent e) {
                                    super.componentResized(e);
                                    splitPane.setDividerLocation(0.20);
                                }
                            });

                            pb.updateValue(100, 100);
                            pb.dispose();

                            JFrame f = new JFrame("TIME DURATION PREVIEW BETWEEN [" + xVariable.getSelectedItem().toString() + "] AND [" + values.getSelectedItem().toString() + "]");
                            f.setLayout(new BorderLayout(2,2));
                            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                            f.getContentPane().add(splitPane, BorderLayout.CENTER);
                            f.pack();
                            f.setVisible(true);

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

            values = new JComboBox();
            values.addItem("");
            values.addItem("YEAR");
            values.addItem("MONTH");
            values.addItem("DAY");
            values.addItem("DAY OF WEEK");
            values.setBackground(Color.WHITE);
            values.setFont(MyDirectGraphVars.tahomaPlainFont11);
            values.setFocusable(false);

            pivot = new JComboBox();
            pivot.addItem("");
            pivot.setBackground(Color.WHITE);
            pivot.setFont(MyDirectGraphVars.tahomaPlainFont11);
            pivot.setFocusable(false);

            JPanel xPanel = new JPanel();
            xPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel variableLabel1 = new JLabel("X:");
            variableLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);
            xPanel.add(variableLabel1);
            xPanel.add(xVariable);

            JPanel yPanel = new JPanel();
            yPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel variableLabel2 = new JLabel("V.:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);
            yPanel.add(variableLabel2);
            yPanel.add(values);

            JLabel emptyLabel = new JLabel("       ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            result.setFont(MyDirectGraphVars.tahomaBoldFont12);

            orderBy = new JCheckBox("ORDER BY");
            orderBy.setFont(MyDirectGraphVars.tahomaPlainFont12);
            orderBy.setFocusable(false);
            orderBy.setSelected(false);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
            controlPanel.add(xPanel);
            controlPanel.add(emptyLabel);
            controlPanel.add(yPanel);
            controlPanel.add(orderBy);
            controlPanel.add(showBtn);
            controlPanel.add(result);

            TitledBorder border = BorderFactory.createTitledBorder("YEAR/MONTH/DAY PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        try {
            if (searchTxt.getText().length() > 0) {
                String searchValue = searchTxt.getText();
                searchTable(searchValue);
            }
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

    private JFreeChart setChart(LinkedHashMap<String, Long> valueMap, JTable distributionTable, JTable propertyTable) {
        try {
            long total = 0L;
            maxFreq = 0;
            minFreq = 100000000000000L;
            for (String key : valueMap.keySet()) {
                if (maxFreq < valueMap.get(key)) {
                    maxFreq = valueMap.get(key);
                }

                if (minFreq > valueMap.get(key)) {
                    minFreq = valueMap.get(key);
                }
                total += valueMap.get(key);
            }
            if (orderBy.isSelected()) {
                valueMap = MyDirectGraphSysUtil.sortMapByLongValue(valueMap);
            }
            avg = (float) total/valueMap.size();

            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MIN.", "" + minFreq});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"MAX.", "" + maxFreq});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"AVG.", "" + MyDirectGraphMathUtil.threeDecimalFormat(avg)});
            ((DefaultTableModel)propertyTable.getModel()).addRow(new String[]{"STD.", ""});

            int i=1;
            for (String key : valueMap.keySet()) {
                ((DefaultTableModel) distributionTable.getModel()).addRow(new String[]{
                        "" + (i++),
                        "" + key,
                        "" + valueMap.get(key),
                        "" + MyDirectGraphMathUtil.threeDecimalFormat((double)valueMap.get(key)/maxFreq),
                        "" + MyDirectGraphMathUtil.threeDecimalPercent((double)valueMap.get(key)/dataTbl.getRowCount())
                });
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();

            for (String key : valueMap.keySet()) {
                dataset.addValue(valueMap.get(key), "", key);
            }

            String plotTitle = "";
            String xaxis = "";
            String yaxis = "";
            PlotOrientation orientation = PlotOrientation.VERTICAL;
            boolean show = false;
            boolean toolTips = true;
            boolean urls = false;

            return ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls);
        } catch (Exception ex) {}
        return null;
    }


}
