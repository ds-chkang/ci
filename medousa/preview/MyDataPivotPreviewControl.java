package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class MyDataPivotPreviewControl
extends JPanel
implements ActionListener {

    private JTable dataTbl;
    private JComboBox variable1;
    private JComboBox variable2;

    private JComboBox value;
    private JButton showBtn;

    public MyDataPivotPreviewControl() {}

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(3,3));

            if (dataTbl == null) {dataTbl = dataTable;}

            variable1 = new JComboBox();
            variable1.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable1.addItem(columns[i]);
            }
            variable1.setBackground(Color.WHITE);
            variable1.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable1.setFocusable(false);
            variable1.addActionListener(this);

            value = new JComboBox();
            value.setBackground(Color.WHITE);
            value.setFont(MyDirectGraphVars.tahomaPlainFont11);
            value.setFocusable(false);

            variable2 = new JComboBox();
            variable2.addItem("");
            for (int i=1; i < columns.length; i++) {
                variable2.addItem(columns[i]);
            }
            variable2.setBackground(Color.WHITE);
            variable2.setFont(MyDirectGraphVars.tahomaPlainFont11);
            variable2.setFocusable(false);

            JLabel variableLabel1 = new JLabel("PIVOT:");
            variableLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JLabel variableLabel2 = new JLabel("TARGET:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);

            showBtn = new JButton("SHOW");
            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(this);

            JLabel variable1EmptyLabel = new JLabel("  ");
            variable1EmptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel variable1Panel = new JPanel();
            variable1Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            variable1Panel.add(variableLabel1);
            variable1Panel.add(variable1);
            variable1Panel.add(variable1EmptyLabel);

            JPanel variable2Panel = new JPanel();
            variable2Panel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            variable2Panel.add(variableLabel2);
            variable2Panel.add(variable2);

            JLabel pivotValueLabel = new JLabel("VALUE:");
            pivotValueLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            JPanel valuePanel = new JPanel();
            valuePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            valuePanel.add(pivotValueLabel);
            valuePanel.add(value);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
            controlPanel.add(variable1Panel);
            controlPanel.add(valuePanel);
            controlPanel.add(variable2Panel);
            controlPanel.add(showBtn);

            TitledBorder border = BorderFactory.createTitledBorder("PIVOT PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
           // ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override public void run() {
                if (e.getSource() == variable1) {
                    MyProgressBar pb = new MyProgressBar(false);
                    try {
                        if (variable1.getSelectedIndex() == 0) return;

                        Set<String> columnValues = new HashSet<>();
                        int row = dataTbl.getRowCount();
                        while (row > 0) {
                            columnValues.add(dataTbl.getValueAt(row - 1, variable1.getSelectedIndex()).toString());
                            row--;
                        }

                        value.removeAllItems();
                        value.addItem("");
                        for (String value : columnValues) {
                            if (value.matches("\\d+(\\.\\d+)?")) {
                                MyDataPivotPreviewControl.this.value.addItem((long) Float.parseFloat(value));
                            } else {
                                MyDataPivotPreviewControl.this.value.addItem(value);
                            }
                        }

                        value.revalidate();
                        value.repaint();
                        pb.updateValue(100, 100);
                        pb.dispose();
                    } catch (Exception ex) {
                        pb.updateValue(100, 100);
                        pb.dispose();
                    }
                } else if (e.getSource() == showBtn) {
                    if (variable1.getSelectedItem().toString().trim().equals(variable2.getSelectedItem().toString().trim())) {
                        MyMessageUtil.showInfoMsg("Choose different variables.");
                    } else {
                        ChartPanel chartPanel = new ChartPanel(setPivotChart());
                        chartPanel.getChart().getCategoryPlot().setRangeGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setDomainGridlinePaint(Color.DARK_GRAY);
                        chartPanel.getChart().getCategoryPlot().setBackgroundPaint(Color.WHITE);//setBackgroundPaint(new Color(0,0,0,0.05f));
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setTickLabelFont(MyDirectGraphVars.f_pln_8);
                        chartPanel.getChart().getCategoryPlot().getDomainAxis().setLabelFont(MyDirectGraphVars.f_pln_8);
                        chartPanel.getChart().getCategoryPlot().getRangeAxis().setTickLabelFont(MyDirectGraphVars.f_pln_8);
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
                        chartPanel.setBorder(BorderFactory.createLoweredBevelBorder());

                        JFrame f = new JFrame("PIVOT PREVIEW");
                        f.setLayout(new BorderLayout(3,3));
                        f.getContentPane().add(chartPanel, BorderLayout.CENTER);
                        f.pack();
                        f.setVisible(true);
                    }
                }
            }
        }).start();
    }

    private JFreeChart setPivotChart() {
        MyProgressBar pb = new MyProgressBar(false);
        try {
            LinkedHashMap<Long, Long> numericValueMap = null;
            LinkedHashMap<String, Long> stringValueMap = null;

            int row = dataTbl.getRowCount() - 1;
            while (row > -1) {
                if (!dataTbl.getValueAt(0, variable1.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                    String pivotValue = dataTbl.getValueAt(row, variable1.getSelectedIndex()).toString();
                    if (pivotValue.equals(this.value.getSelectedItem().toString())) {
                        if (dataTbl.getValueAt(0, variable2.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                            if (numericValueMap == null) numericValueMap = new LinkedHashMap<>();
                            long targetValue = (long) Float.parseFloat(dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString());
                            if (numericValueMap.containsKey(targetValue)) {
                                numericValueMap.put(targetValue, numericValueMap.get(targetValue) + 1);
                            } else {
                                numericValueMap.put(targetValue, 1L);
                            }
                        } else {
                            if (stringValueMap == null) stringValueMap = new LinkedHashMap<>();
                            String targetValue = dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString();
                            if (stringValueMap.containsKey(targetValue)) {
                                stringValueMap.put(targetValue, stringValueMap.get(targetValue) + 1);
                            } else {
                                stringValueMap.put(targetValue, 1L);
                            }
                        }
                    }
                } else {
                    long pivotValue = (long) (Float.parseFloat(dataTbl.getValueAt(row, variable1.getSelectedIndex()).toString().split("\\.")[0]));
                    if (pivotValue == Long.parseLong(this.value.getSelectedItem().toString())) {
                        if (!dataTbl.getValueAt(0, variable2.getSelectedIndex()).toString().matches("\\d+(\\.\\d+)?")) {
                            if (stringValueMap == null) stringValueMap = new LinkedHashMap<>();
                            String targetValue = dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString();
                            if (stringValueMap.containsKey(targetValue)) {
                                stringValueMap.put(targetValue, stringValueMap.get(targetValue) + 1);
                            } else {
                                stringValueMap.put(targetValue, 1L);
                            }
                        } else {
                            if (numericValueMap == null) numericValueMap = new LinkedHashMap<>();
                            long targetValue = Long.parseLong(dataTbl.getValueAt(row, variable2.getSelectedIndex()).toString().split("\\.")[0]);
                            if (numericValueMap.containsKey(targetValue)) {
                                numericValueMap.put(targetValue, numericValueMap.get(targetValue) + 1);
                            } else {
                                numericValueMap.put(targetValue, 1L);
                            }
                        }
                    }
                }
                row--;
            }

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            if (numericValueMap != null) {
                numericValueMap = MyDirectGraphSysUtil.sortMapByLongKeyByLongValue(numericValueMap);
                pb.updateValue(50, 100);

                for (Long key : numericValueMap.keySet()) {
                    dataset.addValue(numericValueMap.get(key), "", key);
                }
            } else {
                stringValueMap = MyDirectGraphSysUtil.sortMapByLongValue(stringValueMap);
                pb.updateValue(50, 100);

                for (String key : stringValueMap.keySet()) {
                    dataset.addValue(stringValueMap.get(key), "", key);
                }
            }

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
            ex.printStackTrace();
            pb.updateValue(100, 100);
            pb.dispose();
        }
        return null;
    }

}
