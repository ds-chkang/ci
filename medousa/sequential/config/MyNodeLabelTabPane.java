package medousa.sequential.config;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MyNodeLabelTabPane
extends MyTabPane {

    private final String TAB_TITLE = " NODE LABELS   ";
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private MyNodeLabelTable nodeLabelTable;

    public MyNodeLabelTabPane() {
        super();
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setTable();
        this.setFont(TAB_TITLE_FONT);
        this.addTab(TAB_TITLE, this.tablePanel);
        this.setFocusable(false);
    }

    public void setTable() {
        this.nodeLabelTable = new MyNodeLabelTable() {@Override
            public TableCellRenderer getCellRenderer(int row, int column) {return new TableCellRenderer();}
        };
        this.tableScrollPane = new JScrollPane(this.nodeLabelTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 180));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(0,0));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    public MyNodeLabelTable getTable() {
        return this.nodeLabelTable;
    }

    class TableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component rendererComp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            rendererComp.setForeground(Color.BLACK);
            rendererComp.setBackground(Color.WHITE);
            return rendererComp ;
        }
    }
}