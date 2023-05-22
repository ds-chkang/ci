package medousa.sequential.graph.listener;

import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyNodeLabelSelecterListener
implements ActionListener {

    private JComboBox nodeLabelExcludeSelecter;
    private JComboBox nodeLabelValueExcludeSelecter;

    public MyNodeLabelSelecterListener(JComboBox nodeLabelExcludeSelecter, JComboBox nodeLabelValueExcludeSelecter) {
        this.nodeLabelExcludeSelecter = nodeLabelExcludeSelecter;
        this.nodeLabelValueExcludeSelecter = nodeLabelValueExcludeSelecter;
    }

    @Override public void actionPerformed(ActionEvent ae) {
        new Thread(new Runnable() {
            @Override public void run() {
                String itemSelected = nodeLabelExcludeSelecter.getSelectedItem().toString();
                Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
                Set<String> nodeLabelValueSet = new HashSet<>();
                for (MyNode n : nodes) {
                    if (n.nodeLabelMap.containsKey(itemSelected)) {
                        nodeLabelValueSet.add(n.nodeLabelMap.get(itemSelected));
                    }
                }
                
                nodeLabelValueExcludeSelecter.removeAllItems();
                nodeLabelValueExcludeSelecter.addItem("");
                for (String nodeLabelValue : nodeLabelValueSet) {
                    nodeLabelValueExcludeSelecter.addItem(nodeLabelValue);
                }
                nodeLabelValueExcludeSelecter.revalidate();
                nodeLabelValueExcludeSelecter.repaint();
            }
        }).start();
    }
}
