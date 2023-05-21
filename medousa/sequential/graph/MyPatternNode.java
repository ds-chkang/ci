package medousa.sequential.graph;

import java.io.Serializable;

public class MyPatternNode
extends MyDepthNode implements Serializable {

    public long time;
    public long paths;


    public MyPatternNode(String name, int depth) {
        super(name, depth);
    }

}
