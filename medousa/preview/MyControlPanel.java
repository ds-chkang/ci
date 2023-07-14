package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyControlPanel
extends JPanel {

    private MyPivotPreview pivotDataPreviewControl = new MyPivotPreview();
    private MyRealRealCorrelationPreview realRealCorrelationPreview = new MyRealRealCorrelationPreview();
    private MyVariableQuantizer columnQuantizer = new MyVariableQuantizer();
    private MyYearMonthDayPreviewer yearMonthDayPreviewer = new MyYearMonthDayPreviewer();
    private MySequenceIntervalPreviewer intervalPreviewer = new MySequenceIntervalPreviewer();
    private MyDurationPreview durationPreview = new MyDurationPreview();
    public MyColumnStatisticsTable columnStatisticsTable = new MyColumnStatisticsTable();
    public MyCategoryRealCorrelationPreview categoryRealCorrelationPreview = new MyCategoryRealCorrelationPreview();
    public MyRealValueBoxPlotPreview realValueBoxPlotPreview = new MyRealValueBoxPlotPreview();

    public MyControlPanel() {}

    public void decorate(JTable dataTbl) {
        try {
            removeAll();
            setLayout(new BorderLayout(1, 1));

            pivotDataPreviewControl.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            realRealCorrelationPreview.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            yearMonthDayPreviewer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            columnQuantizer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            intervalPreviewer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            durationPreview.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            categoryRealCorrelationPreview.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            realValueBoxPlotPreview.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);

            JSplitPane pivotQuantizerSplitpane = new JSplitPane();
            pivotQuantizerSplitpane.setOneTouchExpandable(false);
            pivotQuantizerSplitpane.setDividerLocation(0.55);
            pivotQuantizerSplitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            pivotQuantizerSplitpane.setTopComponent(pivotDataPreviewControl);
            pivotQuantizerSplitpane.setBottomComponent(columnQuantizer);
            pivotQuantizerSplitpane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    pivotQuantizerSplitpane.setDividerLocation(0.55);
                }
            });

            JSplitPane yearMonthRealRealCorrelationSplitPane = new JSplitPane();
            yearMonthRealRealCorrelationSplitPane.setOneTouchExpandable(false);
            yearMonthRealRealCorrelationSplitPane.setDividerLocation(0.45);
            yearMonthRealRealCorrelationSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            yearMonthRealRealCorrelationSplitPane.setTopComponent(realRealCorrelationPreview);
            yearMonthRealRealCorrelationSplitPane.setBottomComponent(yearMonthDayPreviewer);
            yearMonthRealRealCorrelationSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    yearMonthRealRealCorrelationSplitPane.setDividerLocation(0.45);
                }
            });

            JSplitPane rightTopSplitPane = new JSplitPane();
            rightTopSplitPane.setOneTouchExpandable(false);
            rightTopSplitPane.setDividerLocation(0.55);
            rightTopSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            rightTopSplitPane.setTopComponent(pivotQuantizerSplitpane);
            rightTopSplitPane.setBottomComponent(yearMonthRealRealCorrelationSplitPane);
            rightTopSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    rightTopSplitPane.setDividerLocation(0.55);
                }
            });

            JSplitPane intervalDurationSplitPane = new JSplitPane();
            intervalDurationSplitPane.setOneTouchExpandable(false);
            intervalDurationSplitPane.setDividerLocation(0.65);
            intervalDurationSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            intervalDurationSplitPane.setTopComponent(intervalPreviewer);
            intervalDurationSplitPane.setBottomComponent(durationPreview);
            intervalDurationSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    intervalDurationSplitPane.setDividerLocation(0.65);
                }
            });

            JSplitPane categoryRealRealBoxPlotSplitPane = new JSplitPane();
            categoryRealRealBoxPlotSplitPane.setOneTouchExpandable(false);
            categoryRealRealBoxPlotSplitPane.setDividerLocation(0.6);
            categoryRealRealBoxPlotSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            categoryRealRealBoxPlotSplitPane.setTopComponent(categoryRealCorrelationPreview);
            categoryRealRealBoxPlotSplitPane.setBottomComponent(realValueBoxPlotPreview);
            categoryRealRealBoxPlotSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    categoryRealRealBoxPlotSplitPane.setDividerLocation(0.6);
                }
            });


            JSplitPane leftTopSplitPane = new JSplitPane();
            leftTopSplitPane.setOneTouchExpandable(false);
            leftTopSplitPane.setDividerLocation(0.65);
            leftTopSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            leftTopSplitPane.setTopComponent(intervalDurationSplitPane);
            leftTopSplitPane.setBottomComponent(categoryRealRealBoxPlotSplitPane);
            leftTopSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    leftTopSplitPane.setDividerLocation(0.65);
                }
            });

            JSplitPane topSplitPane = new JSplitPane();
            topSplitPane.setOneTouchExpandable(false);
            topSplitPane.setDividerLocation(0.51);
            topSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            topSplitPane.setLeftComponent(leftTopSplitPane);
            topSplitPane.setRightComponent(rightTopSplitPane);
            topSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    topSplitPane.setDividerLocation(0.51);
                }
            });

            this.columnStatisticsTable.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);

            JSplitPane controlSplitPane = new JSplitPane();
            controlSplitPane.setOneTouchExpandable(false);
            controlSplitPane.setDividerLocation(0.76);
            controlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            controlSplitPane.setTopComponent(topSplitPane);
            controlSplitPane.setBottomComponent(this.columnStatisticsTable);
            controlSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    controlSplitPane.setDividerLocation(0.76);
                }
            });

            add(controlSplitPane, BorderLayout.CENTER);
        } catch (Exception ex) {ex.printStackTrace();}
    }
}
