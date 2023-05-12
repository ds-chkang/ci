package medousa.sequential.utils;

import medousa.sequential.graph.MyNode;

import java.util.Collection;

public class MyDepthNodeExcluder {

    public MyDepthNodeExcluder() {}

    public static void exludeDepthNodes(int depth) {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        float max = 0f;
        for (MyNode n : nodes) {
            if (n.getCurrentValue() > 0) {
                if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 1) {
                    if (n.nodeDepthInfoMap.containsKey(depth)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 2) {
                    if (!n.nodeDepthInfoMap.containsKey(depth)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 3) {
                    if (!n.nodeDepthInfoMap.containsKey(depth-1)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                } else if (MySequentialGraphVars.getSequentialGraphViewer().vc.depthExcludeSymbolSelecter.getSelectedIndex() == 4) {
                    if (!n.nodeDepthInfoMap.containsKey(depth+1)) {
                        n.setOriginalValue(n.getCurrentValue());
                        n.setCurrentValue(0);
                    } else if (max < n.getCurrentValue()) {
                        max = n.getCurrentValue();
                    }
                }
            }
        }

        if (max > 0) {
            MySequentialGraphVars.g.MX_N_VAL = max;
            MySequentialGraphVars.getSequentialGraphViewer().excluded = true;
        }
    }

}
