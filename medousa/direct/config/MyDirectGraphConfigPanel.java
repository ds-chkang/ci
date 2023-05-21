package medousa.direct.config;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class MyDirectGraphConfigPanel
extends JPanel
implements Serializable {

    private MyDirectGraphNodeValueTabPane nodeValueTabPane;
    private MyDirectGraphEdgeValueTabPane edgeValueTabPane;
    private MyDirectGraphEdgeLabelTabPane edgeLabelTabPane;
    private MyDirectDefaultVariableTabPane defaultVariableTabPane;
    private MyDirectGraphNodeLabelTabPane nodeLabelTabPane;
    private JPanel container = new JPanel();
    private JPanel configWestPanel = new JPanel();
    private JPanel configEastPanel = new JPanel();
    private JPanel southPanel = new JPanel();
    private JPanel northPanel = new JPanel();
    private JPanel westPanel = new JPanel();
    private JPanel eastPanel = new JPanel();

    public MyDirectGraphConfigPanel() {
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

            this.configWestPanel.setBounds(new Rectangle(60, 40, 620, 640));
            this.configEastPanel.setBounds(new Rectangle(735, 35, 620, 650));
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
        this.edgeValueTabPane = new MyDirectGraphEdgeValueTabPane();
        this.configWestPanel.add(this.edgeValueTabPane, BorderLayout.CENTER);
    }

    private void setNodeValueTable() {
        nodeValueTabPane = new MyDirectGraphNodeValueTabPane();
        this.configEastPanel.add(nodeValueTabPane, BorderLayout.CENTER);
    }

    private void setNodeLabelTable() {
        nodeLabelTabPane = new MyDirectGraphNodeLabelTabPane();
        this.configEastPanel.add(nodeLabelTabPane, BorderLayout.SOUTH);
    }

    private void setEdgeNameTable() {
        edgeLabelTabPane = new MyDirectGraphEdgeLabelTabPane();
        this.configWestPanel.add(edgeLabelTabPane, BorderLayout.SOUTH);
    }

    public MyDirectGraphDefaultVariableTable getDefaultVariableTable() {
        return defaultVariableTabPane.getTable();
    }
    public MyDirectGraphNodeLabelTable getNodeLabelTable() { return this.nodeLabelTabPane.getTable();}
    public MyDirectGraphNodeValueTable getNodeValueTable() { return this.nodeValueTabPane.getTable();}
    public MyDirectGraphEdgeValueTable getEdgeValueTable() {return this.edgeValueTabPane.getTable();}
    public MyDirectGraphEdgeLabelTable getEdgeLabelTable() {return this.edgeLabelTabPane.getTable();}
    public ArrayList<ArrayList<String>> getNodeValueData() {return nodeValueTabPane.getNodeValueData();}
    public MyDirectGraphEdgeLabelTable getEdgeNameTable() {return this.edgeLabelTabPane.getTable();}
    public int getNodeValueHeaderIndex(String header) {return nodeValueTabPane.getNodeValueHeaderIndex(header);}
}
