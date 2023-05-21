package medousa.sequential.config;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.Serializable;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MyEdgeLabelTabPane
extends MyTabPane
implements Serializable {

    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private MyEdgeLabelTable edgeLabelTable;
    private final String TAB_TITLE = " EDGE LABELS   ";

    public MyEdgeLabelTabPane() {
        super();
        decorate();
    }

    @Override
    protected void decorate() {
        this.setTable();
        this.setFont(TAB_TITLE_FONT);
        this.setBackground(Color.decode("#d6d9df"));
        this.addTab(TAB_TITLE, this.tablePanel);
        this.setFocusable(false);
    }

    private void setTable() {
        this.edgeLabelTable = new MyEdgeLabelTable() {@Override
            public TableCellRenderer getCellRenderer(int row, int column) {return new TableCellRenderer();
            }
        };
        this.tableScrollPane = new JScrollPane(this.edgeLabelTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(390, 180));
        this.tablePanel = new JPanel();
        this.tablePanel.setLayout(new BorderLayout(0,0));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    public MyEdgeLabelTable getTable() {
        return edgeLabelTable;
    }

    class TableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            Component rendererComp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            rendererComp.setForeground(Color.BLACK);
            rendererComp.setBackground(Color.WHITE);//Color.decode("#d6d9df"));
            return rendererComp ;
        }

    }
}
