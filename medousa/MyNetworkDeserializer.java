package medousa;

import medousa.direct.graph.layout.MyDirectGraphFRLayout;
import medousa.message.MyMessageUtil;
import medousa.direct.utils.MyDirectGraphSysUtil;
import medousa.direct.utils.MyDirectGraphVars;

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
            fc.showOpenDialog(MyDirectGraphVars.app);

            if (fc.getSelectedFile().isFile()) {
                this.pb = new MyProgressBar(false);
                MyDirectGraphVars.app.getToolBar().getSearchPathButton().setEnabled(true);
                pb.updateValue(20, 100);
                pb.updateValue(40, 100);
                MyDirectGraphSysUtil.loadVariablesAndValues();
                pb.updateValue(60, 100);
                if (this.deserializeNetwork(fc.getSelectedFile()))
                {
                    pb.updateValue(80, 100);
                    MyDirectGraphVars.app.setDirectGraph(MyDirectGraphVars.app.getDirectGraphMsgBroker().createDirectGraphView(
                        new MyDirectGraphFRLayout<>(
                            MyDirectGraphVars.directGraph,
                            new Dimension(6000, 6000)
                            ),
                            new Dimension(6000, 6000))
                    );
                    MyDirectGraphVars.app.getContentTabbedPane().setSelectedIndex(2);
                    pb.updateValue(100, 100);
                    MyDirectGraphSysUtil.sleepExecution(150);
                    pb.dispose();
                    MyDirectGraphVars.app.revalidate();
                    MyDirectGraphVars.app.repaint();
                }
            }
        } catch (Exception ex) {
            this.pb.updateValue(100, 100);
            this.pb.dispose();
            ex.printStackTrace();
        }
    }

    public String loadSerializedNetwork(boolean flag) {// This function gets called from the command prompt.{
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.showOpenDialog(MyDirectGraphVars.app);
            if (fc.getSelectedFile().isFile()) {
                this.pb = new MyProgressBar(false);
                MyDirectGraphVars.app.getToolBar().getSearchPathButton().setEnabled(true);
                pb.updateValue(20, 100);
                pb.updateValue(40, 100);
                MyDirectGraphSysUtil.loadVariablesAndValues();
                pb.updateValue(60, 100);
                if (this.deserializeNetwork(fc.getSelectedFile(), true)) {
                    pb.updateValue(100, 100);
                    MyDirectGraphSysUtil.sleepExecution(120);
                    pb.dispose();
                } else {
                    return "FAILED TO LOAD THE NETWORK!";
                }
            }
        } catch (Exception ex) {
            this.pb.updateValue(100, 100);
            this.pb.dispose();
            ex.printStackTrace();
        }
        return "THE NETWORK HAS SUCCESSFULLY BEEN LOADED INTO MEMORY.";
    }

    private boolean deserializeNetwork(File serializedNetworkFile, boolean flag) { // Gets called from the command prompt.
        try {
            FileInputStream fileInputStream = new FileInputStream(serializedNetworkFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MySerializableNetwork serializableNetwork = (MySerializableNetwork) objectInputStream.readObject();
            //MyVars.plusMarkovChain = serializableNetwork.();
        } catch (Exception ex) {
            this.pb.updateValue(100, 100);
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
           // MyVars.markovChain = serializableNetwork.getMainGraph();
        } catch (Exception ex) {
            this.pb.updateValue(100, 100);
            this.pb.dispose();
            MyMessageUtil.showErrorMsg("The selected file is not a serialized network.\n" +
                                                            "Select a serialized network file.");
            return false;
        }
        return true;
    }
}
