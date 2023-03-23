package datamining.config;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class MyDirectConfigPanel
extends JPanel
implements Serializable {

    private MyDirectNodeValueTabPane nodeValueTabPane;
    private MyDirectEdgeValueTabPane edgeValueTabPane;
    private MyDirectEdgeLabelTabPane edgeLabelTabPane;
    private MyDirectDefaultVariableTabPane defaultVariableTabPane;
    private MyDirectNodeLabelTabPane nodeLabelTabPane;
    private JPanel container = new JPanel();
    private JPanel configWestPanel = new JPanel();
    private JPanel configEastPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel northPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    private JPanel eastPanel = new JPanel();

    public MyDirectConfigPanel() {
        try {
            this.northPanel.setBackground(Color.decode("#D6D9DF"));
            this.southPanel.setBackground(Color.decode("#D6D9DF"));
            this.eastPanel.setBackground(Color.decode("#D6D9DF"));
            this.westPanel.setBackground(Color.decode("#D6D9DF"));

            this.southPanel.setPreferredSize(new Dimension(5, 5));
            this.northPanel.setPreferredSize(new Dimension(5, 5));
            this.eastPanel.setPreferredSize(new Dimension(5, 5));
            this.westPanel.setPreferredSize(new Dimension(5, 5));

            this.container.setLayout(new BorderLayout(0, 0));
            this.container.add(southPanel, BorderLayout.SOUTH);
            this.container.add(northPanel, BorderLayout.NORTH);
            this.container.add(westPanel, BorderLayout.WEST);
            this.container.add(eastPanel, BorderLayout.EAST);
            this.container.setBackground(Color.decode("#D6D9DF"));

            this.configWestPanel.setBounds(new Rectangle(60, 40, 620, 670));
            this.configEastPanel.setBounds(new Rectangle(735, 35, 620, 600));
            this.configWestPanel.setLayout(new BorderLayout(20, 15));

            this.setDefaultVariableTable();
            this.setEdgeValueTable();
            this.setEdgeNameTable();
            this.setNodeValueTable();
            this.setNodeLabelTable();

            this.setLayout(new BorderLayout());
            this.add(this.container);

            JPanel configPanel = new JPanel();
            configPanel.setBackground(Color.decode("#D6D9DF"));
            configPanel.setLayout(null);
            configPanel.add(configEastPanel);
            configPanel.add(configWestPanel);
            configPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JScrollPane scrollPane = new JScrollPane(configPanel);
            scrollPane.setBackground(Color.decode("#D6D9DF"));
            this.container.add(scrollPane, BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }

    private void setDefaultVariableTable() {
        defaultVariableTabPane = new MyDirectDefaultVariableTabPane();
        this.configWestPanel.add(defaultVariableTabPane, BorderLayout.NORTH);
    }

    private void setEdgeValueTable() {
        this.edgeValueTabPane = new MyDirectEdgeValueTabPane();
        this.configWestPanel.add(this.edgeValueTabPane, BorderLayout.CENTER);
    }

    private void setNodeValueTable() {
        nodeValueTabPane = new MyDirectNodeValueTabPane();
        this.configEastPanel.add(nodeValueTabPane, BorderLayout.CENTER);
    }

    private void setNodeLabelTable() {
        nodeLabelTabPane = new MyDirectNodeLabelTabPane();
        this.configEastPanel.add(nodeLabelTabPane, BorderLayout.SOUTH);
    }

    private void setEdgeNameTable() {
        edgeLabelTabPane = new MyDirectEdgeLabelTabPane();
        this.configWestPanel.add(edgeLabelTabPane, BorderLayout.SOUTH);
    }

    public MyDirectDefaultVariableTable getDefaultVariableTable() {
        return defaultVariableTabPane.getTable();
    }
    public MyDirectNodeLabelTable getNodeLabelTable() { return this.nodeLabelTabPane.getTable();}
    public MyDirectNodeValueTable getNodeValueTable() { return this.nodeValueTabPane.getTable();}
    public MyDirectEdgeValueTable getEdgeValueTable() {return this.edgeValueTabPane.getTable();}
    public MyDirectEdgeLabelTable getEdgeLabelTable() {return this.edgeLabelTabPane.getTable();}
    public ArrayList<ArrayList<String>> getNodeValueData() {return nodeValueTabPane.getNodeValueData();}
    public MyDirectEdgeLabelTable getEdgeNameTable() {return this.edgeLabelTabPane.getTable();}
    public int getNodeValueHeaderIndex(String header) {return nodeValueTabPane.getNodeValueHeaderIndex(header);}
}
