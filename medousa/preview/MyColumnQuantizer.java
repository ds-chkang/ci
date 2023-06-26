package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import medousa.sequential.category.MySequentialGraphCategory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

public class MyColumnQuantizer
extends JPanel
implements ActionListener {

    private JComboBox variable;
    private JTextField number;
    private JButton showBtn;
    private JTable dataTbl;
    private JTextField searchTxt = new JTextField();
    private TableRowSorter<TableModel> sorter;
    private JCheckBox orderBy;

    public MyColumnQuantizer() {}

    public void decorate(String [] columns, JTable dataTable) {
        removeAll();
        setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));

        this.dataTbl = dataTable;

        JPanel variablePanel = new JPanel();
        variablePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));

        JLabel variableLabel = new JLabel("TARGET  ");
        variableLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

        variable = new JComboBox();
        variable.setMinimumSize(new Dimension(130, 24));
        variable.setFocusable(false);
        variable.setBackground(Color.WHITE);
        variable.setFont(MyDirectGraphVars.tahomaPlainFont12);
        variable.addItem("");
        for (int i=1; i < columns.length; i++) {
            variable.addItem("" + columns[i]);
        }
        variable.addActionListener(this);

        JLabel numberLabel = new JLabel("NO. OF Q.");
        numberLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

        number = new JTextField();
        number.setFont(MyDirectGraphVars.tahomaPlainFont12);
        number.setPreferredSize(new Dimension(130, 24));
        number.setBorder(BorderFactory.createEtchedBorder());

        JPanel qPanel = new JPanel();
        qPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
        qPanel.add(numberLabel);
        qPanel.add(number);

        variablePanel.add(variableLabel);
        variablePanel.add(variable);

        orderBy = new JCheckBox("ORDER BY VALUE ");
        orderBy.setFont(MyDirectGraphVars.tahomaPlainFont12);
        orderBy.setFocusable(false);
        orderBy.setSelected(false);

        showBtn = new JButton("SHOW");
        showBtn.setFocusable(false);
        showBtn.setBackground(Color.WHITE);
        showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
        showBtn.addActionListener(this);

        TitledBorder border = BorderFactory.createTitledBorder("QUANTIZATION PREVIEW");
        border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
        setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

        add(variablePanel);
        add(qPanel);
        add(orderBy);
        add(showBtn);

    }


    @Override public void actionPerformed(ActionEvent e) {
        if (e.getSource() == variable) {
            String variableValue = dataTbl.getValueAt(0, variable.getSelectedIndex()).toString();
            if (!variableValue.matches("\\d+(\\.\\d+)?")) {
                MyMessageUtil.showInfoMsg("Select a numeric column.");
            }
        } else if (e.getSource() == showBtn) {
            if (number.getText().length() == 0 ||
                !number.getText().matches("\\d+(\\.\\d+)?")) {
                MyMessageUtil.showInfoMsg("Check the number of quantization");
                return;
            }

            MyProgressBar pb = new MyProgressBar(false);
            long max = 0L;
            long min = 10000000000L;
            int categories = Integer.parseInt(number.getText());

            java.util.List<Long> columnsValues = new ArrayList<>();
            int row = dataTbl.getRowCount();
            while (row > 0) {
                long value = (long) Float.parseFloat(dataTbl.getValueAt(row-1, variable.getSelectedIndex()).toString());

                if (max < value) {
                    max = value;
                }

                if (min > value) {
                    min = value;
                }
                columnsValues.add(value);
                row--;
            }

            MySequentialGraphCategory quantizer = new MySequentialGraphCategory(min, max, categories);
            quantizer.setCategoryIntervals(columnsValues);
            java.util.List<Integer> quantizations = quantizer.getCategoryIntervals();
            showChart(quantizations, pb);
        }
    }

    private void showChart(java.util.List<Integer> quantizations, MyProgressBar pb) {
        //System.out.println(quantizations);
        LinkedHashMap<Long, Long> valueMap = new LinkedHashMap<>();
        for (int quantization : quantizations) {
            valueMap.put((long) quantization, 0L);
        }

        int row = dataTbl.getRowCount() - 1;
        while (row > -1) {
            long value = (long) Float.parseFloat(dataTbl.getValueAt(row, variable.getSelectedIndex()).toString().trim().split("\\.")[0]);
            for (int quantization : quantizations) {
                if (value <= quantization) {
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
        if (orderBy.isSelected()) {
            valueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(valueMap);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (Long key : valueMap.keySet()) {
            dataset.addValue(valueMap.get(key), "", key);
        }

        String plotTitle = "";
        String xaxis = "";
        String yaxis = "";
        PlotOrientation orientation = PlotOrientation.VERTICAL;
        boolean show = false;
        boolean toolTips = true;
        boolean urls = false;

        ChartPanel chartPanel = new ChartPanel(ChartFactory.createBarChart(plotTitle, xaxis, yaxis, dataset, orientation, show, toolTips, urls));
        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.f_pln_8);
        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_12);
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

        String [] columns = {"NO.", "NAME", "VALUE"};
        String [][] data = {};

        DefaultTableModel model = new DefaultTableModel(data, columns);
        JTable distributionTable = new JTable(model);
        distributionTable.setRowHeight(24);
        distributionTable.setFont(MyDirectGraphVars.f_pln_12);
        distributionTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(70);
        distributionTable.setBackground(Color.WHITE);
        distributionTable.setFocusable(false);
        distributionTable.getTableHeader().setBackground(new Color(0,0,0,0));
        distributionTable.getTableHeader().setOpaque(false);
        distributionTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        this.sorter = new TableRowSorter<>(distributionTable.getModel());
        distributionTable.setRowSorter(this.sorter);

        int i=1;
        for (long key : valueMap.keySet()) {
            model.addRow(new String[]{
                "" + (i++),
                "" + key,
                "" + valueMap.get(key)
            });
        }

        JButton searchBtn = new JButton("SEARCH");
        searchBtn.setFocusable(false);
        this.searchTxt.setFont(MyDirectGraphVars.f_pln_12);
        this.searchTxt.setBorder(BorderFactory.createEtchedBorder());
        this.searchTxt.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                e.setKeyChar(Character.toUpperCase(keyChar));
            }
        });
        JPanel dataSearchPanel = new JPanel();
        dataSearchPanel.setLayout(new BorderLayout(1,1));
        dataSearchPanel.add(this.searchTxt, BorderLayout.CENTER);
        dataSearchPanel.add(searchBtn, BorderLayout.EAST);
        dataSearchPanel.setBorder(BorderFactory.createLoweredSoftBevelBorder());
        searchBtn.addActionListener(this::searchButtonActionPerformed);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(new JScrollPane(distributionTable));
        splitPane.setRightComponent(chartPanel);
        splitPane.setDividerLocation(0.17);
        splitPane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                splitPane.setDividerLocation(0.17);
            }
        });

        pb.updateValue(100, 100);
        pb.dispose();

        JFrame f = new JFrame("QUANTIZED VARIABLE DISTRIBUTION");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                f.setAlwaysOnTop(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                f.setAlwaysOnTop(false);
            }
        });
        f.setLayout(new BorderLayout(2,2));
        f.getContentPane().add(splitPane, BorderLayout.CENTER);
        f.pack();
        f.setAlwaysOnTop(true);
        f.setVisible(true);
    }

    private void searchButtonActionPerformed(ActionEvent e) {
        String searchValue = searchTxt.getText();
        searchTable(searchValue);
    }

    private void searchTable(String searchValue) {
        RowFilter<TableModel, Object> rowFilter = RowFilter.regexFilter(searchValue);
        this.sorter.setRowFilter(rowFilter);
    }
}
