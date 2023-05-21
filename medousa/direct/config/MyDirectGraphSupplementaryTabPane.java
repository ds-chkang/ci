package medousa.direct.config;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Created by changhee on 17. 7. 17.
 */
public class MyDirectGraphSupplementaryTabPane
extends MyDirectGraphTabPane {

    private final String TAB_TITLE = "SUPPLEMENTARY VARIABLES    ";
    private JPanel tablePanel;
    private JScrollPane tableScrollPane;
    private MyDirectGraphSupplementaryTable supplemenVariableTable;


    public MyDirectGraphSupplementaryTabPane() {
        super();
        this.decorate();
    }

    @Override
    protected void decorate() {
        this.setTable();
        this.setFont(TAB_TITLE_FONT);
        this.addTab(TAB_TITLE, this.tablePanel);
        this.setEnabledAt(0, true);
        this.setFocusable(false);
    }

    public void setTable() {
        this.supplemenVariableTable = new MyDirectGraphSupplementaryTable() {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new TableCellRenderer();
            }
        };
        this.tableScrollPane = new JScrollPane(this.supplemenVariableTable);
        this.tableScrollPane.setBorder(
                BorderFactory.
                        createEtchedBorder(
                                Color.decode("#bcd1e4"),
                                Color.decode("#bfd4ed")
                        )
        );
        this.tableScrollPane.setPreferredSize(new Dimension(360, 280));
        this.tablePanel = new JPanel();
        this.tablePanel.setBorder(
                BorderFactory.
                        createLineBorder(Color.BLACK));
        this.tablePanel.setLayout(new BorderLayout(0,0));
        this.tablePanel.add(this.tableScrollPane, BorderLayout.CENTER);
    }

    public MyDirectGraphSupplementaryTable getTable() {
        return this.supplemenVariableTable;
    }
    public void updateTable(String [] headers, boolean isUpdate) { this.supplemenVariableTable.update(headers, isUpdate); }

    class TableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {
            Component rendererComp =
                    super.getTableCellRendererComponent(
                            table,
                            value,
                            isSelected,
                            hasFocus,
                            row,
                            column);
            //Set foreground color
            rendererComp.setForeground(Color.BLACK);
            //Set background color
            rendererComp.setBackground(Color.decode("#ffffff"));
            return rendererComp ;
        }
    }

}