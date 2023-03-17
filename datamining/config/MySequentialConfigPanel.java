package datamining.config;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Created by changhee on 17. 7. 12.
 */
public class MySequentialConfigPanel
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

    public MySequentialConfigPanel() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                try {
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

                    configWestPanel.setBounds(new Rectangle(60, 40, 620, 670));
                    configEastPanel.setBounds(new Rectangle(735, 40, 620, 670));
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
                    JScrollPane scrollPane = new JScrollPane(configPanel);
                    scrollPane.setBackground(Color.decode("#D6D9DF"));
                    container.add(scrollPane, BorderLayout.CENTER);
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
