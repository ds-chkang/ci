package datamining.graph;

import java.util.HashSet;
import java.util.Set;

public class MyPatternNode
extends MyDepthNode {

    public long time;

    public Set<Integer> sequences = new HashSet<>();

    public MyPatternNode(String name, int depth) {
        super(name, depth);
    }

}
