package medousa.preview;

import medousa.MyProgressBar;
import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyTableCellColumnRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MyColumnStatisticsTable
extends JPanel {

    private JTable columnStatTable;
    private JPopupMenu popupMenu;
    private static JFrame f;

    public MyColumnStatisticsTable() {}

    public void decorate(String [] dataColumns, JTable dataTable) {
        removeAll();
        setLayout(new BorderLayout(1,1));

        String [] statColumns = new String[dataColumns.length+1];
        String [][] statData = {};

        statColumns[0] = "";
        for (int i=1; i < dataColumns.length; i++) {statColumns[i] = dataColumns[i];}

        DefaultTableModel columnStatModel = new DefaultTableModel(statData, statColumns);

        // Counts.
        String [] countValues = new String[dataColumns.length+1];
        countValues[0] = "COUNT";
        for (int i=1; i < statColumns.length; i++) {
            countValues[i] = MyDirectGraphMathUtil.getCommaSeperatedNumber(dataTable.getRowCount());
        }
        columnStatModel.addRow(countValues);
        columnStatModel.addRow(getNumberOfUniqueValues(dataTable));
        columnStatModel.addRow(getDuplicatedValues(dataTable));
        columnStatModel.addRow(getMedianValues(dataTable));
        columnStatModel.addRow(getMinValues(dataTable));
        columnStatModel.addRow(getMaxValues(dataTable));
        columnStatModel.addRow(getAverageValues(dataTable));
        columnStatModel.addRow(getStdValues(dataTable));
        columnStatModel.addRow(getTwentyFifthPositionedNumber(dataTable));
        columnStatModel.addRow(getFiftythPositionedNumber(dataTable));
        columnStatModel.addRow(getSeventyFifthPositionedNumber(dataTable));

        this.columnStatTable = new JTable(columnStatModel);
        columnStatTable.setRowHeight(24);
        columnStatTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        columnStatTable.getColumnModel().getColumn(0).setMaxWidth(70);
        columnStatTable.setFont(MyDirectGraphVars.f_pln_12);
        columnStatTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        columnStatTable.setBackground(Color.WHITE);
        columnStatTable.setFocusable(false);
        columnStatTable.getTableHeader().setBackground(new Color(0, 0, 0, 0));
        columnStatTable.getTableHeader().setOpaque(false);
        columnStatTable.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        columnStatTable.getColumnModel().getColumn(0).setCellRenderer(new MyTableCellColumnRenderer());
        columnStatTable.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        columnStatTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                String [] tooltips = {
                        "COUNT",
                        "UNIQUE VALUE",
                        "DUPLICATED VALUES",
                        "MEDIAN",
                        "MINIMUM VALUE",
                        "MAXIMUM VALUE",
                        "AVERAGE",
                        "STANDARD DEVIATION",
                        "25th Value",
                        "50th Value",
                        "75th Value"
                };
                int row = columnStatTable.rowAtPoint(e.getPoint());
                int column = columnStatTable.columnAtPoint(e.getPoint());
                columnStatTable.setToolTipText(tooltips[row]);
            }
        });

        // Create a JPopupMenu
        popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("ENLARGE");
        deleteItem.addActionListener(e -> {
            new Thread(new Runnable() {
                @Override public void run() {
                    MyProgressBar pb = new MyProgressBar(false);
                    enlarge(dataColumns, dataTable);
                    pb.updateValue(100, 100);
                    pb.dispose();
                }
            }).start();
        });
        popupMenu.add(deleteItem);
        // Add the MouseListener to the JTable
        columnStatTable.addMouseListener(new TableMouseListener());

        JScrollPane tableScrollPane = new JScrollPane(columnStatTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("COLUMN PROPERTIES"));
        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void enlarge(String [] dataColumns, JTable dataTable) {
        MyColumnStatisticsTable columnStatisticsTable = new MyColumnStatisticsTable();
        columnStatisticsTable.decorate(dataColumns, dataTable);

        f = new JFrame("COLUMN PROPERTIES");
        f.setLayout(new BorderLayout(1,1));
        f.getContentPane().add(columnStatisticsTable, BorderLayout.CENTER);
        f.setPreferredSize(new Dimension(700, 400));
        f.pack();
        f.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                f = null;
            }
        });
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.setVisible(true);
    }

    private class TableMouseListener
    implements MouseListener {
        @Override public void mouseClicked(MouseEvent e) {
            if (SwingUtilities.isRightMouseButton(e)) {
                if (f != null) return;
                int row = columnStatTable.rowAtPoint(e.getPoint());
                int column = columnStatTable.columnAtPoint(e.getPoint());

                if (row >= 0 && row < columnStatTable.getRowCount()) {
                    //columnStatTable.setRowSelectionInterval(row, row);
                    //columnStatTable.setColumnSelectionInterval(column, column);
                    columnStatTable.setSelectionBackground(Color.WHITE);
                    columnStatTable.setSelectionForeground(Color.BLACK);
                    popupMenu.show(columnStatTable, e.getX(), e.getY());
                }
            } else {
                columnStatTable.setSelectionBackground(Color.ORANGE);
                columnStatTable.setSelectionForeground(Color.BLACK);
            }
        }

        @Override public void mousePressed(MouseEvent e) {}
        @Override public void mouseReleased(MouseEvent e) {}
        @Override public void mouseEntered(MouseEvent e) {
            columnStatTable.setSelectionBackground(Color.WHITE);
            columnStatTable.setSelectionForeground(Color.BLACK);
        }
        @Override public void mouseExited(MouseEvent e) {
            columnStatTable.setSelectionBackground(Color.WHITE);
            columnStatTable.setSelectionForeground(Color.BLACK);
        }
    }

    private String [] getDuplicatedValues(JTable dataTable) {
        String[] duplicateValues = new String[dataTable.getColumnCount() + 1];
        duplicateValues[0] = "DUP.";
        for (int i = 1; i < duplicateValues.length; i++) {
            Map<String, Integer> values = new HashMap();
            int row = 0;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row, i-1).toString();
                if (values.containsKey(columnValue)) {
                    values.put(columnValue, values.get(columnValue)+1);
                } else {
                    values.put(columnValue, 1);
                }
                row++;
            }

            if (values.size() > 0) {
                int duplicatedValueCount = 0;
                for (String key : values.keySet()) {
                    if (values.get(key) > 1) {
                        duplicatedValueCount++;
                    }
                }

                duplicateValues[i] = MyDirectGraphMathUtil.getCommaSeperatedNumber(duplicatedValueCount);
            } else {
                duplicateValues[i] = "0";
            }
        }
        return duplicateValues;
    }

    private String [] getMedianValues(JTable dataTable) {
        String [] medianValues = new String[dataTable.getColumnCount()+1];
        medianValues[0] = "MEDIAN";
        for (int i = 1; i < medianValues.length; i++) {
            float[] values = new float[dataTable.getRowCount()];
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    medianValues[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    values[row++] = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                Arrays.sort(values);
                int length = values.length;
                int middleIndex = length / 2;
                if (length % 2 == 0) {
                    float middleElement1 = values[middleIndex-1];
                    float middleElement2 = values[middleIndex];
                    medianValues[i] = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat((middleElement1 + middleElement2) / 2.0));
                } else {
                    medianValues[i] = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(values[middleIndex]));
                }
            }
        }
        return medianValues;
    }

    private String [] getTwentyFifthPositionedNumber(JTable dataTable) {
        String [] twentyFifthPositionedNumbers = new String[dataTable.getColumnCount()+1];
        twentyFifthPositionedNumbers[0] = "25TH";
        for (int i = 1; i < twentyFifthPositionedNumbers.length; i++) {
            float[] values = new float[dataTable.getRowCount()];
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    twentyFifthPositionedNumbers[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    values[row++] = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                Arrays.sort(values);
                // Calculate the index position for 1 quarter (25%)
                int quarterIndex = (int) Math.round(values.length*0.25);

                // Retrieve the value at the quarter index position
                float quarterValue = values[quarterIndex-1];
                twentyFifthPositionedNumbers[i] = MyDirectGraphMathUtil.twoDecimalFormat(quarterValue);
            }
        }
        return twentyFifthPositionedNumbers;
    }

    private String [] getFiftythPositionedNumber(JTable dataTable) {
        String [] fiftythPositionedNumbers = new String[dataTable.getColumnCount()+1];
        fiftythPositionedNumbers[0] = "50TH";
        for (int i = 1; i < fiftythPositionedNumbers.length; i++) {
            float[] values = new float[dataTable.getRowCount()];
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    fiftythPositionedNumbers[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    values[row++] = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                Arrays.sort(values);
                // Calculate the index position for 1 quarter (25%)
                int quarterIndex = (int) Math.round(values.length*0.5);

                // Retrieve the value at the quarter index position
                float quarterValue = values[quarterIndex-1];
                fiftythPositionedNumbers[i] = MyDirectGraphMathUtil.twoDecimalFormat(quarterValue);
            }
        }
        return fiftythPositionedNumbers;
    }

    private String [] getSeventyFifthPositionedNumber(JTable dataTable) {
        String [] seventyFifthPositionedNumbers = new String[dataTable.getColumnCount()+1];
        seventyFifthPositionedNumbers[0] = "75TH";
        for (int i = 1; i < seventyFifthPositionedNumbers.length; i++) {
            float[] values = new float[dataTable.getRowCount()];
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    seventyFifthPositionedNumbers[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    values[row++] = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                Arrays.sort(values);
                // Calculate the index position for 1 quarter (25%)
                int quarterIndex = (int) Math.round(values.length*0.75);

                // Retrieve the value at the quarter index position
                float quarterValue = values[quarterIndex-1];
                seventyFifthPositionedNumbers[i] = MyDirectGraphMathUtil.twoDecimalFormat(quarterValue);
            }
        }
        return seventyFifthPositionedNumbers;
    }

    private String [] getNumberOfUniqueValues(JTable dataTable) {
        String [] uniqueValues = new String[dataTable.getColumnCount()+1];
        uniqueValues[0] = "UNIQ.";
        for (int i=1; i < uniqueValues.length; i++) {
            int row = 0;
            Set<String> valueSet = new HashSet<>();
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row++, i-1).toString();
                if (valueSet.contains(columnValue)) {
                    valueSet.add(columnValue);
                } else {
                    valueSet.add(columnValue);
                }
            }
            uniqueValues[i] = MyDirectGraphMathUtil.getCommaSeperatedNumber(valueSet.size());
        }
        return uniqueValues;
    }

    private String [] getMinValues(JTable dataTable) {
        String [] minValues = new String[dataTable.getColumnCount()+1];
        minValues[0] = "MIN.";
        for (int i=1; i < minValues.length; i++) {
            float min = 10000000000f;
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row++, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    minValues[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else if (min > Float.parseFloat(columnValue)) {
                    min = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                minValues[i] = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(min));
            }
        }
        return minValues;
    }

    private String [] getMaxValues(JTable dataTable) {
        String [] maxValues = new String[dataTable.getColumnCount()+1];
        maxValues[0] = "MAX.";
        for (int i=1; i < maxValues.length; i++) {
            float max = -100000000000f;
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row++, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    maxValues[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else if (max < Float.parseFloat(columnValue)) {
                    max = Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                maxValues[i] = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(max));
            }
        }
        return maxValues;
    }

    private String [] getAverageValues(JTable dataTable) {
        String [] avgValues = new String[dataTable.getColumnCount()+1];
        avgValues[0] = "AVG.";
        for (int i=1; i < avgValues.length; i++) {
            float total = 0f;
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row++, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    avgValues[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    total += Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                avgValues[i] = MyDirectGraphSysUtil.formatAverageValue(
                   MyDirectGraphMathUtil.twoDecimalFormat(total/dataTable.getRowCount()));
            }
        }
        return avgValues;
    }


    private String [] getStdValues(JTable dataTable) {
        String [] stdValues = new String[dataTable.getColumnCount()+1];
        stdValues[0] = "STD.";
        for (int i=1; i < stdValues.length; i++) {
            float total = 0f;
            int row = 0;
            boolean earlyStopped = false;
            while (row < dataTable.getRowCount()) {
                String columnValue = dataTable.getValueAt(row++, i-1).toString();
                if (!columnValue.matches("-?\\d+(\\.\\d+)?")) {
                    stdValues[i] = "N/A";
                    earlyStopped = true;
                    break;
                } else {
                    total += Float.parseFloat(columnValue);
                }
            }

            if (!earlyStopped) {
                float avg = total/dataTable.getRowCount();
                float sum = 0f;
                row = 0;
                while (row < dataTable.getRowCount()) {
                    String columnValue = dataTable.getValueAt(row++, i-1).toString();
                    sum += Math.pow(Float.parseFloat(columnValue) - avg, 2);
                }
                float std = (float) Math.sqrt(sum/dataTable.getRowCount());
                stdValues[i] = MyDirectGraphSysUtil.formatAverageValue(MyDirectGraphMathUtil.twoDecimalFormat(std));
            }
        }
        return stdValues;
    }

}

