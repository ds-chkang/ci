package datamining.graph;

import datamining.utils.system.MyVars;
import java.util.*;

public class MyConnectedNetworkComponentFinder {
    public static void setConnectedComponentsByGraph() {
        Set<MyDirectNode> visited = new HashSet<>();
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getVertices();
        for (MyDirectNode n : nodes) {
            if (!visited.contains(n)) {
                Set<MyDirectNode> component = new HashSet<>();
                dfs(n, visited, component);
                MyVars.connectedComponentCountsByGraph.add(component);
            }
        }
    }

    private static void dfs(MyDirectNode node, Set<MyDirectNode> visitedSet, Set<MyDirectNode> componentSet) {
        visitedSet.add(node);
        componentSet.add(node);
        Collection<MyDirectNode> nodes = MyVars.directMarkovChain.getNeighbors(node);
        for (MyDirectNode neighbor : nodes) {
            if (!visitedSet.contains(neighbor)) {
                dfs(neighbor, visitedSet, componentSet);
            }
        }
    }

}