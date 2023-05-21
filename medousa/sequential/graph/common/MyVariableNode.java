package medousa.sequential.graph.common;

public class MyVariableNode implements Comparable<MyVariableNode> {
    private String name;
    private int value;

    public MyVariableNode() {}
    public void setName(String name) { this.name = name; }
    public void setValue(int value) {this.value = value; }
    public String getName() { return this.name; }
    public int getValue() { return this.value; }

    @Override
    public int compareTo(MyVariableNode o) {
        return (this.value - o.value)*1000;
    }
}