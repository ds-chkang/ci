package medousa.sequential.broker;

import medousa.sequential.config.MySequentialGraphConfigPanel;

public class MySequentialGraphConfigBroker {

    public MySequentialGraphConfigBroker() {}
    private MySequentialGraphConfigPanel sequentialConfigPanel = new MySequentialGraphConfigPanel();
    public void setSequentialConfigPanel(MySequentialGraphConfigPanel sequentialConfigPanel) { this.sequentialConfigPanel = sequentialConfigPanel; }
    public MySequentialGraphConfigPanel getConfigPanel() { return this.sequentialConfigPanel; }


}
