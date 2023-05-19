package medousa.sequential.graph.funnel;

import medousa.sequential.graph.MyNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyAnalysisGraphSuccessorPathExtractor {

    private MyAnalysisGraph graph;
    private MyNode selectedNode;

    public MyAnalysisGraphSuccessorPathExtractor(MyAnalysisGraph graph, MyNode selectedNode) {
        this.graph = graph;
        this.selectedNode = selectedNode;
    }

    private List<List<MyNode>> findAllPaths(MyNode start, MyNode end) {
        List<List<MyNode>> allPaths = new ArrayList<>();
        List<MyNode> currentPath = new ArrayList<>();
        currentPath.add(start);

        dfs(start, end, currentPath, allPaths);

        return allPaths;
    }

    private void dfs(MyNode current, MyNode end, List<MyNode> currentPath, List<List<MyNode>> paths) {
        if (current.equals(end)) {
            paths.add(new ArrayList<>(currentPath));
            return;
        }

        Collection<MyNode> neighbors = graph.getSuccessors(current);
        for (MyNode successor : neighbors) {
            if (!currentPath.contains(successor)) {
                currentPath.add(successor);
                dfs(successor, end, currentPath, paths);
                currentPath.remove(currentPath.size() - 1);
            }
        }
    }

    public List<List<MyNode>> run(MyNode startNode) {
        List<List<MyNode>> paths = findAllPaths(startNode, selectedNode);
        return paths;
    }
}
