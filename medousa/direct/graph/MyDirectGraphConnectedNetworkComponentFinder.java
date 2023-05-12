package medousa.direct.graph;

import medousa.direct.utils.MyDirectGraphVars;
import java.util.*;

public class MyDirectGraphConnectedNetworkComponentFinder {
    public static void setConnectedComponentsByGraph() {
        Set<MyDirectNode> visited = new HashSet<>();
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getVertices();
        for (MyDirectNode n : nodes) {
            if (!visited.contains(n)) {
                Set<MyDirectNode> component = new HashSet<>();
                dfs(n, visited, component);
                MyDirectGraphVars.connectedComponentCountsByGraph.add(component);
            }
        }
    }

    private static void dfs(MyDirectNode node, Set<MyDirectNode> visitedSet, Set<MyDirectNode> componentSet) {
        visitedSet.add(node);
        componentSet.add(node);
        Collection<MyDirectNode> nodes = MyDirectGraphVars.directGraph.getNeighbors(node);
        for (MyDirectNode neighbor : nodes) {
            if (!visitedSet.contains(neighbor)) {
                dfs(neighbor, visitedSet, componentSet);
            }
        }
    }

}