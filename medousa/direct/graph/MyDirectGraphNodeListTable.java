package medousa.direct.graph;

import medousa.direct.utils.MyDirectGraphVars;
import medousa.table.MyNodeListTableCellColorRenderer;
import medousa.table.MyTableUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public abstract class MyDirectGraphNodeListTable
extends JFrame
implements ActionListener {

    /**
     * Declare and initialize necessary graphical components and variables.
     */
    protected JButton select = new JButton("SELECT");
    protected JButton save = new JButton("SAVE");
    protected JPanel dataSearchAndSelectPanel = new JPanel();
    protected DefaultTableModel dtm;
    protected JTable table;
    protected JScrollPane predecessors_scroll_pane;
    protected String border_title;
    protected String[] encoded_names;
    protected JTextField searchTxt = new JTextField();
    private boolean removeHightLight = false;


    public MyDirectGraphNodeListTable() {
        super();
    }
    public MyDirectGraphNodeListTable(String title) {
        super(title);
    }

    /**
     * Decorate the frame.
     * Receives columns names from classes inheriting it.
     *
     * @param columns
     */
    protected void decorate(String[] columns) {
        /**
         * Set size and layout.
         */
        this.setPreferredSize(new Dimension(600, 500));
        this.setLayout(new BorderLayout(3, 3));

        /**
         * Initialize and create table, table model, and sorter.
         */
        this.dtm = new DefaultTableModel(new String[][]{}, columns);
        this.table = new JTable(this.dtm);

        DefaultTableCellRenderer tableRenderer = new DefaultTableCellRenderer() {
            Font font = MyDirectGraphVars.f_pln_12;
            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value, boolean isSelected, boolean hasFocus,
                    int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                setFont(font);
                JLabel c = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(MyDirectGraphVars.f_pln_12);
                c.setToolTipText(value.toString());

                if (searchTxt.getText().trim().length() > 0 &&
                    !removeHightLight &&
                    column == 1 &&
                    table.getValueAt(row, column).toString().equals(searchTxt.getText().trim())) {
                    table.setRowSelectionInterval(row, row);
                }

                return c;
            }
        };

        this.searchTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (searchTxt.getText().length()==0) {
                    table.clearSelection();
                    removeHightLight = true;
                } else {
                    removeHightLight = false;
                }
            }
        });


        table.setSelectionBackground(Color.PINK);
        table.setSelectionForeground(Color.BLACK);
        for (int i=1; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(tableRenderer);
        }

        this.table.getColumnModel().getColumn(0).setCellRenderer(new MyNodeListTableCellColorRenderer());
        //MyTableUtil.setColumnHeaderRenderer(this.table);
        //MyTableUtil.setTableHeaderRenderer(this.table);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.table.setFont(MyDirectGraphVars.f_pln_13);
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        this.table.setCursor(cursor);
        MyTableUtil.setPreferredTableCellWidth(this.table, 0, 70);
        MyTableUtil.setPreferredTableCellWidth(this.table, 1, 490);
        MyTableUtil.setTableHeaderHeight(this.table, 80, 28);
        this.table.setRowHeight(25);

        /**
         * Set scroll pane and attach the table to it.
         */
        this.predecessors_scroll_pane = new JScrollPane(this.table);
        this.predecessors_scroll_pane.setPreferredSize(new Dimension(350, 600));

        /**
         * Attach the exploration panel to the frame for users to save the records in the table
         * as well as explore the selected node.
         */
        this.setDataSvePanel();

        /**
         * Attach everything to the frame.
         */
        this.getContentPane().add(this.predecessors_scroll_pane, BorderLayout.CENTER);
        this.getContentPane().add(this.dataSearchAndSelectPanel, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.pack();
        this.setAlwaysOnTop(true);
        this.setVisible(true);
    }

    private void setDataSvePanel() {
        this.dataSearchAndSelectPanel = MyTableUtil.getDataSelectPanelForJTable(this, this.searchTxt, this.select, this.dtm, this.table);
    }

    /**
     * This method is commonly implemented by all inheriting classes of it.
     * Inheriting classes will implement this method according to their unique ways of
     * reading in records.
     */
    protected abstract void readInRecords();

    /**
     * User interface actions are performed accordingly.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {}
}
