package datamining.main;

import datamining.graph.layout.MyFRLayout;
import datamining.utils.message.MyMessageUtil;
import datamining.utils.system.MySysUtil;
import datamining.utils.system.MyVars;

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
            fc.showOpenDialog(MyVars.main);

            if (fc.getSelectedFile().isFile()) {
                this.pb = new MyProgressBar(false);
                MyVars.main.getToolBar().getSearchPathButton().setEnabled(true);
                pb.updateValue(20, 100);
                pb.updateValue(40, 100);
                MySysUtil.loadVariablesAndValues();
                pb.updateValue(60, 100);
                if (this.deserializeNetwork(fc.getSelectedFile()))
                {
                    pb.updateValue(80, 100);
                    MyVars.main.setDirectGraph(MyVars.main.getMsgBroker().createDirectGraphView(
                        new MyFRLayout<>(
                            MyVars.directMarkovChain,
                            new Dimension(6000, 6000)
                            ),
                            new Dimension(6000, 6000))
                    );
                    MyVars.main.getContentTabbedPane().setSelectedIndex(2);
                    pb.updateValue(100, 100);
                    MySysUtil.sleepExecution(150);
                    pb.dispose();
                    MyVars.main.revalidate();
                    MyVars.main.repaint();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String loadSerializedNetwork(boolean flag) {// This function gets called from the command prompt.{
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            fc.setPreferredSize(new Dimension(600, 460));
            fc.showOpenDialog(MyVars.main);
            if (fc.getSelectedFile().isFile()) {
                this.pb = new MyProgressBar(false);
                MyVars.main.getToolBar().getSearchPathButton().setEnabled(true);
                pb.updateValue(20, 100);
                pb.updateValue(40, 100);
                MySysUtil.loadVariablesAndValues();
                pb.updateValue(60, 100);
                if (this.deserializeNetwork(fc.getSelectedFile(), true)) {
                    pb.updateValue(100, 100);
                    MySysUtil.sleepExecution(120);
                    pb.dispose();
                } else {
                    return "FAILED TO LOAD THE NETWORK!";
                }
            }
        } catch (Exception ex) {
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
            this.pb.dispose();
            MyMessageUtil.showErrorMsg("The selected file is not a serialized network.\n" +
                                                            "Select a serialized network file.");
            return false;
        }
        return true;
    }
}
