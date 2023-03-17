package datamining.broker;

import datamining.config.MySequentialConfigPanel;

public class MyConfigBroker {

    public MyConfigBroker() {}
    private MySequentialConfigPanel sequentialConfigPanel = new MySequentialConfigPanel();
    public void setSequentialConfigPanel(MySequentialConfigPanel sequentialConfigPanel) { this.sequentialConfigPanel = sequentialConfigPanel; }
    public MySequentialConfigPanel getConfigPanel() { return this.sequentialConfigPanel; }


}
