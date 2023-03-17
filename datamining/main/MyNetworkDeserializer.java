package datamining.main;

import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;
import datamining.graph.layout.MyDelegateLayout;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class MyNetworkDeserializer {

    private MyProgressBar pb;

    public MyNetworkDeserializer() {}
    public void loadSerializedNetwork() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.showOpenDialog(MyVars.app);

            if (fc.getSelectedFile().isFile()) {
                this.pb = new MyProgressBar(false);
                MyVars.app.getToolBar().getSearchPathButton().setEnabled(true);
                pb.updateValue(20, 100);
                pb.updateValue(40, 100);
                MySysUtil.loadVariablesAndValues();
                pb.updateValue(60, 100);
                if (this.deserializeNetwork(fc.getSelectedFile()))
                {
                    pb.updateValue(80, 100);
                    MyVars.app.setGrpahViewer(MyVars.app.getMsgBroker().createPlusGraphView(
                        new MyDelegateLayout<>(
                            MyVars.g,
                            new Dimension(4500, 3500)
                            ),
                            new Dimension(4500, 3500))
                    );
                    MyVars.app.getContentTabbedPane().setSelectedIndex(2);
                    pb.updateValue(100, 100);
                    MySysUtil.sleepExecution(150);
                    pb.dispose();
                    MyVars.app.revalidate();
                    MyVars.app.repaint();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean deserializeNetwork(File serializedNetworkFile, boolean flag) { // Gets called from the command prompt.
        try {
            FileInputStream fileInputStream = new FileInputStream(serializedNetworkFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MySerializableNetwork serializableNetwork = (MySerializableNetwork) objectInputStream.readObject();
            MyVars.g = serializableNetwork.getMainGraph();
        } catch (Exception ex) {
            this.pb.dispose();
            return false;
        }
        return true;
    }

    private boolean deserializeNetwork(File serializedNetworkFile) {
        try {
            FileInputStream fileInputStream = new FileInputStream(serializedNetworkFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MySerializableNetwork serializableNetwork = (MySerializableNetwork) objectInputStream.readObject();
            MyVars.g = serializableNetwork.getMainGraph();
        } catch (Exception ex) {
            this.pb.dispose();
            MyMessageUtil.showErrorMsg("The selected file is not a serialized network.\n" +
                                                            "Select a serialized network file.");
            return false;
        }
        return true;
    }
}
