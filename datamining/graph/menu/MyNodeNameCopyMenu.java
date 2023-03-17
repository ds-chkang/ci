package datamining.graph.menu;

import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyNodeNameCopyMenu
extends JPopupMenu
implements ActionListener {

    private JMenuItem copyNodeName = new JMenuItem("COPY NODE NAME");
    private JMenu download = new JMenu("DOWNLOAD");
    private JMenuItem downloadObjectList = new JMenuItem("OBJECT ID LIST");

    public MyNodeNameCopyMenu() {
        decorate();
    }

    private void decorate() {
        this.setMenuItem(null, this.copyNodeName);
        this.add(new JSeparator());
        this.setMenuItem(null, this.download);
        this.setMenuItem(this.download, this.downloadObjectList);
        this.download.setEnabled(false);
    }

    private void setMenuItem(JMenu root, JMenuItem menuItem) {
        menuItem.setFont(MyVars.f_pln_13);
        menuItem.addActionListener(this);
        if (root == null) {
            this.add(menuItem);
        } else {
            root.add(menuItem);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();
    }

}
