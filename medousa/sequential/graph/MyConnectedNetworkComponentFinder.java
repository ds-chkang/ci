package medousa.sequential.graph;

import medousa.sequential.utils.MySequentialGraphVars;
import java.util.*;

public class MyConnectedNetworkComponentFinder {
    public static void setConnectedComponentsByGraph() {
        Set<MyNode> visited = new HashSet<>();
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            if (!visited.contains(n)) {
                Set<MyNode> componentSet = new HashSet<>();
                dfsWithComponents(n, visited, componentSet);
                MySequentialGraphVars.connectedComponentCountsByGraph.add(componentSet);
            }
        }
    }

    private static void dfsWithComponents(MyNode node, Set<MyNode> visitedSet, Set<MyNode> componentSet) {
        visitedSet.add(node);
        componentSet.add(node);
        Collection<MyNode> nodes = MySequentialGraphVars.g.getNeighbors(node);
        for (MyNode neighbor : nodes) {
            if (!visitedSet.contains(neighbor)) {
                dfsWithComponents(neighbor, visitedSet, componentSet);
            }
        }
    }

}