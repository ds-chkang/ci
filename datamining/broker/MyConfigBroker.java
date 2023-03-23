package datamining.broker;

import datamining.config.MyDirectConfigPanel;

public class MyConfigBroker {

    public MyConfigBroker() {}
    private MyDirectConfigPanel directConfigPanel = new MyDirectConfigPanel();
    public void setDirectConfigPanel(MyDirectConfigPanel directConfigPanel) { this.directConfigPanel = directConfigPanel; }
    public MyDirectConfigPanel getDirectConfigPanel() {return this.directConfigPanel;}

}
