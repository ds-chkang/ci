package medousa.sequential.graph;

import medousa.sequential.graph.MyNode;
import medousa.sequential.utils.MySequentialGraphVars;

import java.util.*;

public class MyAperiodicityChecker {

    public String isAperiodic() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode startNode : nodes) {
            Set<MyNode> visited = new HashSet<>();
            List<Integer> cycleLengths = new ArrayList<>();
            dfs(startNode, startNode, visited, cycleLengths);
            int gcd = computeGCD(cycleLengths);
            if (gcd > 1) {
                return "False";
            }
        }
        return "True";
    }

    private void dfs(MyNode startNode, MyNode currentNode, Set<MyNode> visited, List<Integer> cycleLengths) {
        visited.add(currentNode);
        Collection<MyNode> neighbors = MySequentialGraphVars.g.getSuccessors(currentNode);
        for (MyNode neighbor : neighbors) {
            if (visited.contains(neighbor) && neighbor == startNode) {
                int cycleLength = visited.size();
                cycleLengths.add(cycleLength);
            } else if (!visited.contains(neighbor)) {
                dfs(startNode, neighbor, visited, cycleLengths);
            }
        }
        visited.remove(currentNode);
    }

    private int computeGCD(List<Integer> numbers) {
        if (numbers.size() <= 1) {
            return 2;
        }
        int gcd = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            gcd = gcd(gcd, numbers.get(i));
        }
        return gcd;
    }

    private int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

}
