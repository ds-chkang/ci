package datamining.main;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

public class MyNetworkSerializer
extends JPopupMenu
implements ActionListener, Serializable{

    private JMenuItem createGraph = new JMenuItem("LOAD NETWORK");

    public MyNetworkSerializer() {
        this.setPreferredSize(new Dimension(190, 30));
        this.createGraph.setPreferredSize(new Dimension(150, 30));
        this.createGraph.addActionListener(this);
        this.add(this.createGraph);
    }

    public void actionPerformed(ActionEvent e) {
        if (this.createGraph == e.getSource()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    deSerializeNetwork();;
                }
            }).start();
        }
    }

    public void deSerializeNetwork() {
        MyNetworkDeserializer networkDeserializer = new MyNetworkDeserializer();
        networkDeserializer.loadSerializedNetwork();
    }

    public void serializeNetworkToFile() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setPreferredSize(new Dimension(580, 460));
            fc.showSaveDialog(MyVars.main);
            if (fc.getSelectedFile() != null) {
                MySerializableNetwork serializableNetwork = new MySerializableNetwork();
                if (fc.getSelectedFile().exists()) {
                    fc.getSelectedFile().delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(fc.getSelectedFile());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(serializableNetwork);
                objectOutputStream.close();
                MyMessageUtil.showInfoMsg("The current network has been saved.");
            }
        } catch (Exception ex) {
            MyMessageUtil.showErrorMsg("Failed to save the current network!");
            ex.printStackTrace();
        }
    }
}

class MySerializableNetwork
implements Serializable {



}

class MyNetworkLoaderMouseListener
implements MouseListener {

    private JPanel graphPanel;

    public MyNetworkLoaderMouseListener() {}
    public MyNetworkLoaderMouseListener(JPanel graphPanel) { this.graphPanel = graphPanel; }
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    (new MyNetworkSerializer()).show(graphPanel, e.getX(), e.getY());
                }
            }).start();
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
}

