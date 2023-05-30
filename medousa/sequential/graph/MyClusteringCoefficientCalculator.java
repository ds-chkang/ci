package medousa.sequential.graph;

import medousa.sequential.utils.MySequentialGraphVars;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyClusteringCoefficientCalculator {

    public MyClusteringCoefficientCalculator() {}

    public void calculateClusteringCoefficient() {
        Collection<MyNode> nodes = MySequentialGraphVars.g.getVertices();
        for (MyNode n : nodes) {
            List<MyNode> neighbors = new ArrayList<>(MySequentialGraphVars.g.getNeighbors(n));
            int numNeighbors = neighbors.size();
            int numConnections = 0;

            // Count the number of connections among the neighbors
            for (int i = 0; i < numNeighbors; i++) {
                for (int j = i + 1; j < numNeighbors; j++) {
                    MyNode neighbor1 = neighbors.get(i);
                    MyNode neighbor2 = neighbors.get(j);
                    if (MySequentialGraphVars.g.getNeighbors(neighbor1).contains(neighbor2)) {
                        numConnections++;
                    }
                }
            }

            // Calculate the number of possible connections among the neighbors
            int possibleConnections = (numNeighbors * (numNeighbors - 1)) / 2;

            // Calculate the clustering coefficient
            float clusteringCoefficient = 0.0f;
            if (possibleConnections > 0) {
                clusteringCoefficient = (float) numConnections / possibleConnections;
            }

            n.clusteringCoefficient = clusteringCoefficient;
        }
    }

}
