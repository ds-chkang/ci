package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphVars;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class MyCategoryRealCorrelationPreview
extends JPanel {

    private JTable dataTbl;
    private JComboBox xVariable;
    private JComboBox yVariable;
    private JLabel result = new JLabel();

    private JButton showBtn = new JButton("SHOW");

    public MyCategoryRealCorrelationPreview() {}

    private void showChart() {
        final BoxAndWhiskerCategoryDataset dataset = createDataset();
        final CategoryAxis xAxis = new CategoryAxis(xVariable.getSelectedItem().toString());
        final NumberAxis yAxis = new NumberAxis(yVariable.getSelectedItem().toString());
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

        final JFreeChart chart = new JFreeChart(
                xVariable.getSelectedItem().toString() + "   VS   " + yVariable.getSelectedItem().toString(),
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                true
        );
        chart.removeLegend();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(450, 270));

        JFrame f = new JFrame("CATEGORY  VS  REAL CORRELATION");
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(chartPanel);
        f.setPreferredSize(new Dimension(650, 800));
        f.pack();
        f.setVisible(true);
    }

    private BoxAndWhiskerCategoryDataset createDataset() {
        int row = 0;
        Set<String> categorySet = new HashSet<>();
        while (row < dataTbl.getRowCount()) {
            String value = dataTbl.getValueAt(row, xVariable.getSelectedIndex()).toString();
            categorySet.add(value);
            row++;
        }

        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        int i = 1;
        for (String category : categorySet) {
            List<Double> yValues = new ArrayList();
            int yRow = 0;
            while (yRow < dataTbl.getRowCount()) {
                String xValue = dataTbl.getValueAt(yRow, xVariable.getSelectedIndex()).toString();
                if (xValue.equals(category)) {
                    String yValue = dataTbl.getValueAt(yRow, yVariable.getSelectedIndex()).toString();
                    yValues.add(Double.parseDouble(yValue));
                }
                yRow++;
            }
            i++;
            dataset.add(yValues, "Series" + i,  category);
        }
        return dataset;
    }

    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(1,1));

            if (dataTbl == null) {dataTbl = dataTable;}

            showBtn.setFont(MyDirectGraphVars.tahomaPlainFont12);
            showBtn.setFocusable(false);
            showBtn.setBackground(Color.WHITE);
            showBtn.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    new Thread(new Runnable() {
                        @Override public void run() {
                            MyProgressBar pb = new MyProgressBar(false);
                            showChart();
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

            TitledBorder border = BorderFactory.createTitledBorder("CATEGORY VS REAL CORRELATION PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

}
