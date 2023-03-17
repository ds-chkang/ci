package datamining.config;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MySupplementaryVariableTabPane
extends MyTabPane {

    private final String TAB_TITLE = " SUPPLEMENTARY VARIABLES   ";
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private MySupplementaryVariableTable supplemenVariableTable;

    public MySupplementaryVariableTabPane() {
        super();
        this.decorate();
    }

    @Override protected void decorate() {
        this.setTable();
        this.setFont(TAB_TITLE_FONT);
        this.addTab(TAB_TITLE, this.tablePanel);
        this.setFocusable(false);
    }

    public void setTable() {
        this.supplemenVariableTable = new MySupplementaryVariableTable() {
            @Override public javax.swing.table.TableCellRenderer getCellRenderer(int row, int column) {
                return new MySupplementaryVariableTabPane.MyParameterTableRowHeaderRenderer();
            }
        };
        this.tableScrollPane = new JScrollPane(this.supplemenVariableTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 190));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3, 3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    public MySupplementaryVariableTable getTable() {
        return this.supplemenVariableTable;
    }

    class MyParameterTableRowHeaderRenderer extends JPanel implements TableCellRenderer {
        public MyParameterTableRowHeaderRenderer() {
            this.setLayout(new BorderLayout(0,5));
        }
        @Override public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                JLabel itemLabel = new JLabel(value.toString());
                itemLabel.setVerticalAlignment(CENTER);
                itemLabel.setFont(new Font("Noto Sans", Font.PLAIN, 12));
                this.add(itemLabel);
            }
            return this;
        }
        @Override public void paintComponent(Graphics g) {}
    }
}