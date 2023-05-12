package medousa.sequential.config;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.File;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MyNodeValueTabPane
extends MyTabPane {

    public String [] headers;
    public File dataFile;
    private final String TAB_TITLE = " NODE VALUES   ";
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private MyNodeValueTable nodeValueTable;
    private JButton headerBtn;
    private JButton dataBtn;

    public MyNodeValueTabPane() {
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
        this.nodeValueTable = new MyNodeValueTable() {@Override
        public TableCellRenderer getCellRenderer(int row, int column) {return new TableCellRenderer();}};
        this.tableScrollPane = new JScrollPane(this.nodeValueTable);
        this.tableScrollPane.setBorder(BorderFactory.createEtchedBorder(Color.decode("#bcd1e4"), Color.decode("#bfd4ed")));
        this.tableScrollPane.setPreferredSize(new Dimension(615, 160));
        this.tableScrollPane.setBorder(BorderFactory.createLineBorder(new Color(0.0f, 0.0f, 0.0f, 0.0f)));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(3, 3));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    public MyNodeValueTable getTable() {
        return this.nodeValueTable;
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