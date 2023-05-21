package medousa.test;

import java.util.ArrayList;
import java.util.List;

public class GraphPathExtractor {
    private int numNodes;
    private List<List<Integer>> adjacencyList;

    public GraphPathExtractor(int numNodes) {
        this.numNodes = numNodes;
        adjacencyList = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest) {
        adjacencyList.get(src).add(dest);
    }

    public List<List<Integer>> extractPaths(int startNode, int destinationNode, int desiredPathLength) {
        List<List<Integer>> pathList = new ArrayList<>();
        dfs(startNode, destinationNode, desiredPathLength, new ArrayList<>(), pathList);
        return pathList;
    }

    private void dfs(int currentNode, int destinationNode, int desiredPathLength, List<Integer> currentPath, List<List<Integer>> pathList) {
        currentPath.add(currentNode);

        // Check if current node is the destination or desired path length is reached
        if (currentNode == destinationNode || currentPath.size() == desiredPathLength) {
            pathList.add(new ArrayList<>(currentPath));
        } else {
            for (int neighbor : adjacencyList.get(currentNode)) {
                if (!currentPath.contains(neighbor)) {
                    dfs(neighbor, destinationNode, desiredPathLength, currentPath, pathList);
                }
            }
        }

        currentPath.remove(currentPath.size() - 1);
    }

    public static void main(String[] args) {
        GraphPathExtractor graph = new GraphPathExtractor(6);
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(1, 4);
        graph.addEdge(0, 4);
        graph.addEdge(2, 5);
        graph.addEdge(4, 5);

        int startNode = 0;
        int destinationNode = 4;
        int desiredPathLength = 100;

        List<List<Integer>> paths = graph.extractPaths(startNode, destinationNode, desiredPathLength);
        for (List<Integer> path : paths) {
            System.out.println(path);
        }
    }
}