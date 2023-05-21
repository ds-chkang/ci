package medousa.direct.broker;

import medousa.direct.config.MyDirectGraphConfigPanel;

public class MyDirectGraphConfigBroker {

    public MyDirectGraphConfigBroker() {}
    private MyDirectGraphConfigPanel directConfigPanel = new MyDirectGraphConfigPanel();
    public void setDirectConfigPanel(MyDirectGraphConfigPanel directConfigPanel) { this.directConfigPanel = directConfigPanel; }
    public MyDirectGraphConfigPanel getDirectConfigPanel() {return this.directConfigPanel;}

}
