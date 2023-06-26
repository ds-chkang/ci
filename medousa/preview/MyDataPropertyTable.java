package medousa.preview;

import medousa.direct.utils.MyDirectGraphMathUtil;
import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyDataPropertyTable
extends JPanel {

    public JTable table;
    public DefaultTableModel tableModel;
    public int numberOfColumns = 0;

    public MyDataPropertyTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
            }
        });
    }

    public void decorate() {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(3,3));

        String [] columns = {"PROPERTY", "VALUE"};
        String [][] data = {
                {"COLUMNS", MyDirectGraphMathUtil.getCommaSeperatedNumber(numberOfColumns)}
        };

        tableModel = new DefaultTableModel(data, columns);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setFont(MyDirectGraphVars.f_pln_12);
        table.setFocusable(false);
        table.setRowHeight(24);
        table.getTableHeader().setFont(MyDirectGraphVars.tahomaBoldFont12);
        table.getTableHeader().setBackground(new Color(0,0,0,0));
        table.getTableHeader().setOpaque(false);

        this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

}
