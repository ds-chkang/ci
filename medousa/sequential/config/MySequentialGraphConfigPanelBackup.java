package medousa.sequential.config;

import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.Serializable;

/**
 * Created by changhee on 17. 7. 12.
 */
public class MySequentialGraphConfigPanelBackup
extends JPanel
implements Serializable {

    private MySupplementaryVariableTabPane supplementaryTabPane;
    private MyEdgeValueTabPane edgeValueTabPane;
    private MyEdgeLabelTabPane edgeLabelTabPane;
    private MyNodeValueTabPane nodeValueTablePane;
    private MyNodeLabelTabPane nodeLabelTablePane;
    private MyDefaultVariableTabPane defaultVariableTabPane;
    private JPanel container = new JPanel();
    private JPanel configWestPanel = new JPanel();
    private JPanel configEastPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel northPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    private JPanel eastPanel = new JPanel();
    private TitledBorder dataTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
            "DATA PROPERTIES", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    private TitledBorder headerTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
            "HEADER PROPERTIES", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont12);

    private TitledBorder configTitledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK),
            "CONFIGURATION PROPERTIES", TitledBorder.LEFT, TitledBorder.TOP, MySequentialGraphVars.tahomaBoldFont14);

    public JTable dataTable;
    public JTable headerTable;

    public MySequentialGraphConfigPanelBackup() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
                    JSplitPane configSplitPane = new JSplitPane();
                    configSplitPane.setDividerSize(8
                    );
                    configSplitPane.setResizeWeight(0.14);
                    configSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);

                    northPanel.setBackground(Color.decode("#D6D9DF"));
                    southPanel.setBackground(Color.decode("#D6D9DF"));
                    eastPanel.setBackground(Color.decode("#D6D9DF"));
                    westPanel.setBackground(Color.decode("#D6D9DF"));

                    southPanel.setPreferredSize(new Dimension(5, 5));
                    northPanel.setPreferredSize(new Dimension(5, 5));
                    eastPanel.setPreferredSize(new Dimension(5, 5));
                    westPanel.setPreferredSize(new Dimension(5, 5));

                    container.setLayout(new BorderLayout(0, 0));
                    container.add(southPanel, BorderLayout.SOUTH);
                    container.add(northPanel, BorderLayout.NORTH);
                    container.add(westPanel, BorderLayout.WEST);
                    container.add(eastPanel, BorderLayout.EAST);
                    container.setBackground(Color.decode("#D6D9DF"));

                    configWestPanel.setBounds(new Rectangle(55, 20, 580, 680));
                    configEastPanel.setBounds(new Rectangle(703, 20, 580, 680));
                    configWestPanel.setLayout(new BorderLayout(20, 15));
                    configEastPanel.setLayout(new BorderLayout(20, 18));

                    setDefaultVariableTable();
                    setSupplementaryTable();
                    setEdgeValueTable();
                    setEdgeLabelTable();
                    setNodeValueTable();
                    setNodeLabelTable();
                    setLayout(new BorderLayout());
                    add(container);

                    JPanel configPanel = new JPanel();
                    configPanel.setBackground(Color.decode("#D6D9DF"));
                    configPanel.setLayout(null);
                    configPanel.add(configEastPanel);
                    configPanel.add(configWestPanel);
                    configPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                    String [] headerColumns = {"PROPERTY", "VALUE"};
                    String [][] headerData = {};
                    DefaultTableModel headerModel = new DefaultTableModel(headerData, headerColumns);
                    headerTable = new JTable(headerModel);
                    headerTable.setRowHeight(24);
                    headerTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);
                    headerTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(120);
                    headerTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
                    headerTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
                    JScrollPane headerScrollPane = new JScrollPane(headerTable);
                    JPanel headerPanel = new JPanel();
                    headerPanel.setBorder(headerTitledBorder);
                    headerPanel.setLayout(new BorderLayout(3,3));
                    headerPanel.add(headerScrollPane);

                    String [] dataColumns = {"PROPERTY", "VALUE"};
                    String [][] dataData = {};
                    DefaultTableModel dataModel = new DefaultTableModel(dataData, dataColumns);
                    dataTable = new JTable(dataModel);
                    dataTable.setRowHeight(24);
                    dataTable.getTableHeader().getColumnModel().getColumn(0).setPreferredWidth(100);
                    dataTable.getTableHeader().getColumnModel().getColumn(1).setPreferredWidth(120);
                    dataTable.getTableHeader().setFont(MySequentialGraphVars.tahomaBoldFont12);
                    dataTable.setFont(MySequentialGraphVars.tahomaPlainFont12);
                    JScrollPane dataScrollPane = new JScrollPane(dataTable);
                    JPanel dataPanel = new JPanel();
                    dataPanel.setBorder(dataTitledBorder);
                    dataPanel.setLayout(new BorderLayout(3,3));
                    dataPanel.add(dataScrollPane);

                    GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    int width = gd.getDisplayMode().getWidth();
                    int height = gd.getDisplayMode().getHeight();

                    JSplitPane dataInfoSplitPane = new JSplitPane();
                    dataInfoSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    dataInfoSplitPane.setDividerSize(7);
                    dataInfoSplitPane.setDividerLocation((int)(0.35*height));
                    dataInfoSplitPane.setTopComponent(headerPanel);
                    dataInfoSplitPane.setBottomComponent(dataPanel);
                    dataInfoSplitPane.addComponentListener(new ComponentAdapter() {
                        @Override public void componentResized(ComponentEvent e) {
                            super.componentResized(e);
                            dataInfoSplitPane.setDividerLocation((int)(0.35*height));
                        }
                    });

                    configSplitPane.setLeftComponent(dataInfoSplitPane);
                    configSplitPane.setRightComponent(configPanel);
                    configSplitPane.setDividerLocation((int)(0.146*width));
                    configSplitPane.addComponentListener(new ComponentAdapter() {
                        @Override public void componentResized(ComponentEvent e) {
                            super.componentResized(e);
                            configSplitPane.setDividerLocation((int)(0.146*width));
                        }
                    });

                    container.add(configSplitPane, BorderLayout.CENTER);
                    container.setBorder(configTitledBorder);
                } catch (Exception ex) {ex.printStackTrace();}
            }
        });
    }

    private void setDefaultVariableTable() {
        defaultVariableTabPane = new MyDefaultVariableTabPane();
        configWestPanel.add(defaultVariableTabPane, BorderLayout.NORTH);
    }

   private void setEdgeValueTable() {
        edgeValueTabPane = new MyEdgeValueTabPane();
        configWestPanel.add(edgeValueTabPane, BorderLayout.CENTER);
    }

    private void setEdgeLabelTable() {
        edgeLabelTabPane = new MyEdgeLabelTabPane();
        configWestPanel.add(edgeLabelTabPane, BorderLayout.SOUTH);
    }

    private void setSupplementaryTable() {
        supplementaryTabPane = new MySupplementaryVariableTabPane();
        configEastPanel.add(supplementaryTabPane, BorderLayout.NORTH);
    }

    private void setNodeLabelTable() {
        nodeLabelTablePane = new MyNodeLabelTabPane();
        configEastPanel.add(nodeLabelTablePane, BorderLayout.SOUTH);
    }

    private void setNodeValueTable() {
        nodeValueTablePane = new MyNodeValueTabPane();
        configEastPanel.add(nodeValueTablePane, BorderLayout.CENTER);
    }

    public MyDefaultVariableTable getDefaultVariableTable() {
        return defaultVariableTabPane.getTable();
    }
    public MySupplementaryVariableTable getSupplimentaryVariableTable() { return supplementaryTabPane.getTable(); }
    public MyEdgeValueTable getEdgeValueTable() { return edgeValueTabPane.getTable(); }
    public MyEdgeLabelTable getEdgeLabelTable() { return edgeLabelTabPane.getTable(); }
    public MyNodeValueTable getNodeValueTable() { return nodeValueTablePane.getTable();}
    public MyNodeLabelTable getNodeLabelTable() { return nodeLabelTablePane.getTable();}
}
