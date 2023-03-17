package datamining.graph.listener;

import datamining.graph.MyEdge;
import datamining.utils.system.MyVars;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MyEdgeLabelSelecterListener
implements ActionListener {

    private JComboBox edgeLabelExcludeSelecter;
    private JComboBox edgeLabelValueExcludeSelecter;

    public MyEdgeLabelSelecterListener(JComboBox edgeLabelExcludeSelecter, JComboBox edgeLabelValueExcludeSelecter) {
        this.edgeLabelExcludeSelecter = edgeLabelExcludeSelecter;
        this.edgeLabelValueExcludeSelecter = edgeLabelValueExcludeSelecter;
    }

    @Override public void actionPerformed(ActionEvent ae) {

        String itemSelected = this.edgeLabelExcludeSelecter.getSelectedItem().toString();
        Collection<MyEdge> edges = MyVars.g.getEdges();
        Set<String> edgeLabelValueSet = new HashSet<>();
        for (MyEdge e : edges) {
            if (e.edgeLabelMap.containsKey(itemSelected)) {
                edgeLabelValueSet.add(e.edgeLabelMap.get(itemSelected));
            }
        }

        this.edgeLabelValueExcludeSelecter.removeAllItems();
        this.edgeLabelValueExcludeSelecter.addItem("");
        for (String edgeLabelValue : edgeLabelValueSet) {
            this.edgeLabelValueExcludeSelecter.addItem(edgeLabelValue);
        }

    }
}
