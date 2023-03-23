package datamining.config;

import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MyDirectTabPane
extends JTabbedPane
implements Serializable {

    protected final Font TAB_TITLE_FONT = MyVars.tahomaBoldFont12;

    protected  abstract void decorate();
}
