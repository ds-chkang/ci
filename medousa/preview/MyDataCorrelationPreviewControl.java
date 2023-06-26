package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.message.MyMessageUtil;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyDataCorrelationPreviewControl
extends JPanel {

    private JTable dataTbl;
    private MyDataPreviewChart dataPreviewChart;
    private JComboBox xVariable;
    private JComboBox yVariable;
    private JLabel result = new JLabel();

    private JButton showBtn = new JButton("SHOW");

    public MyDataCorrelationPreviewControl() {}

    public void showCorrelation(int x, int y) {
        double [] xData = new double[dataTbl.getRowCount()];
        double [] yData = new double[dataTbl.getRowCount()];

        int row = 0;
        while (row < dataTbl.getRowCount()) {
            xData[row] = Double.parseDouble((dataTbl.getModel()).getValueAt(row, x).toString());
            yData[row] = Double.parseDouble((dataTbl.getModel()).getValueAt(row, y).toString());
            row++;
        }

        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double correlationCoefficient = correlation.correlation(xData, yData);

        result.setText("");
        result.setText(" CORR.: " + MyDirectGraphMathUtil.fourDecimalFormat(correlationCoefficient));
    }


    public void decorate(String [] columns, JTable dataTable) {
        try {
            removeAll();
            setLayout(new BorderLayout(3,3));

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
                            showCorrelation(xVariable.getSelectedIndex(), yVariable.getSelectedIndex());
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
                            try {
                                if (xVariable.getSelectedIndex() == 0) {
                                    return;
                                }
                                if (dataTable.getRowCount() > 0 && !dataTable.getValueAt(0, xVariable.getSelectedIndex()).toString().trim().matches("\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Select a numerical variable.");
                                }
                            } catch (Exception ex) {

                            }
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
                            try {
                                if (yVariable.getSelectedIndex() == 0) return;

                                if (dataTable.getRowCount() > 0 && !dataTable.getValueAt(0, yVariable.getSelectedIndex()).toString().trim().matches("\\d+(\\.\\d+)?")) {
                                    MyMessageUtil.showInfoMsg("Select a numerical variable.");
                                    yVariable.setSelectedIndex(0);
                                } else {

                                }
                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
            });

            JPanel xPanel = new JPanel();
            xPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel variableLabel1 = new JLabel("X:");
            variableLabel1.setFont(MyDirectGraphVars.tahomaPlainFont12);
            xPanel.add(variableLabel1);
            xPanel.add(xVariable);

            JPanel yPanel = new JPanel();
            yPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2,2));
            JLabel variableLabel2 = new JLabel("Y:");
            variableLabel2.setFont(MyDirectGraphVars.tahomaPlainFont12);
            yPanel.add(variableLabel2);
            yPanel.add(yVariable);

            JLabel emptyLabel = new JLabel("       ");
            emptyLabel.setFont(MyDirectGraphVars.tahomaPlainFont12);

            result.setFont(MyDirectGraphVars.tahomaBoldFont12);

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 3,3));
            controlPanel.add(xPanel);
            controlPanel.add(emptyLabel);
            controlPanel.add(yPanel);
            controlPanel.add(showBtn);
            controlPanel.add(result);

            TitledBorder border = BorderFactory.createTitledBorder("CORRELATION PREVIEW");
            border.setTitleFont(MyDirectGraphVars.tahomaBoldFont12);
            setBorder(BorderFactory.createTitledBorder(border, "", TitledBorder.LEFT, TitledBorder.TOP));

            add(controlPanel, BorderLayout.CENTER);
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

}
