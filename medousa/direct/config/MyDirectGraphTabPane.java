package medousa.direct.config;

import medousa.direct.utils.MyDirectGraphVars;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public abstract class MyDirectGraphTabPane
extends JTabbedPane
implements Serializable {

    protected final Font TAB_TITLE_FONT = MyDirectGraphVars.tahomaBoldFont12;

    protected  abstract void decorate();
}
