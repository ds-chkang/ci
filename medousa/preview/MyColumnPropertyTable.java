package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MyColumnPropertyTable
extends JPanel {

    public JTable table;
    public DefaultTableModel tableModel;
    public int numberOfColumns = 0;

    public MyColumnPropertyTable() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
            }
        });
    }

    public void decorate(
            String values,
            String max,
            String maxCategoryValue,
            String min,
            String minCategoryValue,
            String avg,
            String std
    ) {
        this.removeAll();
        //this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout(2,2));

        String [] columns = {"PROPERTY", "VALUE"};
        String [][] data = {
            {"VALUES", values},
            {"MAX.", max + "(" + maxCategoryValue + ")"},
            {"MIN.", min + "(" + minCategoryValue + ")"},
            {"AVG.", avg},
            {"STD.", std},
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
        table.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(130);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        this.revalidate();
        this.repaint();
    }


}
