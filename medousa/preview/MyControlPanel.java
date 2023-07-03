package medousa.preview;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MyControlPanel
extends JPanel {

    private MyPivotPreview pivotDataPreviewControl = new MyPivotPreview();
    private MyCorrelationPreview correlationPreviewControl = new MyCorrelationPreview();
    private MyVariableQuantizer columnQuantizer = new MyVariableQuantizer();
    private MyYearMonthDayPreviewer yearMonthDayPreviewer = new MyYearMonthDayPreviewer();
    private MySequenceIntervalPreviewer intervalPreviewer = new MySequenceIntervalPreviewer();
    private MyDurationPreview durationPreview = new MyDurationPreview();
    public MyControlPanel() {}

    public void decorate(JTable dataTbl) {
        try {
            removeAll();
            setLayout(new BorderLayout(1, 1));

            pivotDataPreviewControl.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            correlationPreviewControl.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            yearMonthDayPreviewer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            columnQuantizer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            intervalPreviewer.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);
            durationPreview.decorate(MyDirectGraphVars.app.getDirectGraphMsgBroker().getHeaders(), dataTbl);

            JSplitPane pivotQuantizerSplitpane = new JSplitPane();
            pivotQuantizerSplitpane.setOneTouchExpandable(false);
            pivotQuantizerSplitpane.setDividerLocation(0.5);
            pivotQuantizerSplitpane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            pivotQuantizerSplitpane.setTopComponent(pivotDataPreviewControl);
            pivotQuantizerSplitpane.setBottomComponent(columnQuantizer);
            pivotQuantizerSplitpane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    pivotQuantizerSplitpane.setDividerLocation(0.5);
                }
            });

            JSplitPane propertyDataPreviewControlSplitPane = new JSplitPane();
            propertyDataPreviewControlSplitPane.setDividerLocation(.5);
            propertyDataPreviewControlSplitPane.setOneTouchExpandable(false);
            propertyDataPreviewControlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            propertyDataPreviewControlSplitPane.setTopComponent(yearMonthDayPreviewer);
            propertyDataPreviewControlSplitPane.setBottomComponent(correlationPreviewControl);
            propertyDataPreviewControlSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    propertyDataPreviewControlSplitPane.setDividerLocation(.5);
                }
            });

            JSplitPane topFourControlSplitPane = new JSplitPane();
            topFourControlSplitPane.setOneTouchExpandable(false);
            topFourControlSplitPane.setDividerLocation(0.5);
            topFourControlSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            topFourControlSplitPane.setTopComponent(pivotQuantizerSplitpane);
            topFourControlSplitPane.setBottomComponent(propertyDataPreviewControlSplitPane);
            topFourControlSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    topFourControlSplitPane.setDividerLocation(0.5);
                }
            });

            JSplitPane rightSplitPane = new JSplitPane();
            rightSplitPane.setOneTouchExpandable(false);
            rightSplitPane.setDividerLocation(0.8);
            rightSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            rightSplitPane.setTopComponent(topFourControlSplitPane);
            rightSplitPane.setBottomComponent(durationPreview);
            rightSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    rightSplitPane.setDividerLocation(0.8);
                }
            });

            JSplitPane intervalSplitPane = new JSplitPane();
            intervalSplitPane.setOneTouchExpandable(false);
            intervalSplitPane.setDividerLocation(0.5);
            intervalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            intervalSplitPane.setTopComponent(intervalPreviewer);
            intervalSplitPane.setBottomComponent(new JPanel());
            intervalSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    intervalSplitPane.setDividerLocation(0.5);
                }
            });

            JSplitPane controlSplitPane = new JSplitPane();
            controlSplitPane.setOneTouchExpandable(false);
            controlSplitPane.setDividerLocation(0.51);
            controlSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
            controlSplitPane.setLeftComponent(intervalSplitPane);
            controlSplitPane.setRightComponent(rightSplitPane);
            controlSplitPane.addComponentListener(new ComponentAdapter() {
                @Override public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    controlSplitPane.setDividerLocation(0.51);
                }
            });

            add(controlSplitPane, BorderLayout.CENTER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
