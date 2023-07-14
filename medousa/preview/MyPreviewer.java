package medousa.preview;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MyPreviewer
extends JPanel
implements ActionListener {

    public MyChartPreviewer dataPreviewChart = new MyChartPreviewer();
    public MyControlPanel controlPanel = new MyControlPanel();
    public MyDataTablePanel dataTablePanel = new MyDataTablePanel();
    public MyColumnDistributionTable columnDistributionTable = new MyColumnDistributionTable();

    public MyPreviewer() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                removeAll();
            }
        });
    }

    public void decorate() {
        try {
            //this.setBackground(Color.WHITE);
            this.setLayout(new BorderLayout(1, 1));

            this.dataTablePanel.decorate();
            this.controlPanel.decorate(this.dataTablePanel.dataTable);
            this.columnDistributionTable.decorate();
            this.dataPreviewChart.decorate(this.dataTablePanel.dataTable, columnDistributionTable.columnDistributionTable);

            JSplitPane dataDistributionTableChartSplitPane = new JSplitPane();
            dataDistributionTableChartSplitPane.setOneTouchExpandable(false);
            dataDistributionTableChartSplitPane.setDividerLocation(0.25);
            dataDistributionTableChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            dataDistributionTableChartSplitPane.setLeftComponent(this.columnDistributionTable);
            dataDistributionTableChartSplitPane.setRightComponent(dataPreviewChart);
            dataDistributionTableChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    dataDistributionTableChartSplitPane.setDividerLocation(0.25);
                }
            });

            JSplitPane dataTableChartSplitPane = new JSplitPane();
            dataTableChartSplitPane.setOneTouchExpandable(false);
            dataTableChartSplitPane.setDividerLocation(0.685);
            dataTableChartSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            dataTableChartSplitPane.setTopComponent(dataDistributionTableChartSplitPane);
            dataTableChartSplitPane.setBottomComponent(this.dataTablePanel);
            dataTableChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    dataTableChartSplitPane.setDividerLocation(0.685);
                }
            });

            JSplitPane dataPropertyPreviewControlChartSplitPane = new JSplitPane();
            dataPropertyPreviewControlChartSplitPane.setOneTouchExpandable(false);
            dataPropertyPreviewControlChartSplitPane.setDividerLocation(0.33);
            dataPropertyPreviewControlChartSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            dataPropertyPreviewControlChartSplitPane.setLeftComponent(this.controlPanel);
            dataPropertyPreviewControlChartSplitPane.setRightComponent(dataTableChartSplitPane);
            dataPropertyPreviewControlChartSplitPane.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    dataPropertyPreviewControlChartSplitPane.setDividerLocation(0.33);
                }
            });

            this.add(dataPropertyPreviewControlChartSplitPane, BorderLayout.CENTER);
        } catch (Exception ex) {}
    }




    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
