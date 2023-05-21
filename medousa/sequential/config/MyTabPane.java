package medousa.sequential.config;

import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MyTabPane
extends JTabbedPane
implements Serializable {

    protected final Font TAB_TITLE_FONT = MySequentialGraphVars.tahomaBoldFont12;

    protected  abstract void decorate();
}
