package datamining.graph;

import datamining.utils.system.MyVars;
import java.util.*;

public class MyConnectedNetworkComponentFinder {
    public static void setConnectedComponentsByGraph() {
        Set<MyNode> visited = new HashSet<>();
        Collection<MyNode> nodes = MyVars.g.getVertices();
        for (MyNode n : nodes) {
            if (!visited.contains(n)) {
                Set<MyNode> componentSet = new HashSet<>();
                dfsWithComponents(n, visited, componentSet);
                MyVars.connectedComponentCountsByGraph.add(componentSet);
            }
        }
    }

    private static void dfsWithComponents(MyNode node, Set<MyNode> visitedSet, Set<MyNode> componentSet) {
        visitedSet.add(node);
        componentSet.add(node);
        Collection<MyNode> nodes = MyVars.g.getNeighbors(node);
        for (MyNode neighbor : nodes) {
            if (!visitedSet.contains(neighbor)) {
                dfsWithComponents(neighbor, visitedSet, componentSet);
            }
        }
    }

}