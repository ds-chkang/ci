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
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyRealValueBoxPlotPreview
extends JPanel {

    private JTable dataTbl;
    private JComboBox yVariable;
    private JLabel result = new JLabel();

    private JButton showBtn = new JButton("SHOW");

    public MyRealValueBoxPlotPreview() {}

    private void showChart() {
        final BoxAndWhiskerCategoryDataset dataset = createDataset();
        final CategoryAxis xAxis = new CategoryAxis(yVariable.getSelectedItem().toString());
        final NumberAxis yAxis = new NumberAxis(yVariable.getSelectedItem().toString());
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        renderer.setToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        xAxis.setTickLabelFont(new Font("Arial", Font.PLAIN, 0));

        final JFreeChart chart = new JFreeChart(
                yVariable.getSelectedItem().toString(),
                new Font("SansSerif", Font.BOLD, 0),
                plot,
                true
        );
        chart.removeLegend();
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(450, 270));

        JFrame f = new JFrame("REAL VALUE BOXPLOT");
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(chartPanel);
        f.setPreferredSize(new Dimension(650, 800));
        f.pack();
        f.setVisible(true);
    }

    private BoxAndWhiskerCategoryDataset createDataset() {
        final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        int i = 1;
        Set<String> categorySet = new HashSet<>();
        categorySet.add("A");
        categorySet.add("B");
        categorySet.add("C");
        for (String category : categorySet) {
            List<Double> yValues = new ArrayList();
            int yRow = 0;
            while (yRow < dataTbl.getRowCount()) {
                String yValue = dataTbl.getValueAt(yRow, yVariable.getSelectedIndex()).toString();
                yValues.add(Double.parseDouble(yValue));
                yRow++;
            }
            i++;
            if (!category.equals("B")) {
                dataset.add(new ArrayList(), "Series" + i, category);
            } else {
                dataset.add(yValues, "Series" + i, category);
            }
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
                    new Thread(new Runnable() {@Override public void run() {}}).start();
                }
            });

            JPanel yPanel = new JPanel();
            yPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            JLabel variableLabel2 = new JLabel("X:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);
            yPanel.add(variableLabel2);
            yPanel.add(yVariable);

            JLabel emptyLabel = new JLabel("       ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            result.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 1,1));
            controlPanel.add(yPanel);
            controlPanel.add(showBtn);
            controlPanel.add(result);

            TitledBorder border = BorderFactory.createTitledBorder("REAL VALUE BOXPLOT PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));
            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }
}
