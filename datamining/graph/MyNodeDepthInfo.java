package datamining.graph;

public class MyNodeDepthInfo {

    private int contribution;
    private int inContribution;
    private int outContribution;
    private long duration;
    private int durationCount;
    private long reachTime;
    private int reachTimeCount;
    private int predecessors;
    private int successors;

    public int getInContribution() {
        return inContribution;
    }

    public void setInContribution(int inContribution) {
        this.inContribution += inContribution;
    }

    public int getOutContribution() {
        return outContribution;
    }

    public void setOutContribution(int outContribution) {
        this.outContribution += outContribution;
    }

    public long getDuration() {
        return duration;
    }

    public double getAverageDuration() {
        if (this.durationCount == 0) {return 0.00D;}
        return (double)this.duration/this.durationCount;
    }

    public void setDuration(long duration) {
        this.duration += duration;
        this.durationCount++;
    }

    public long getReachTime() {
        return reachTime;
    }

    public double getAverageReachTime() {
        if (this.reachTimeCount == 0) {return 0.00D;}
        return (double)this.reachTime/this.reachTimeCount;
    }

    public void setReachTime(long reachTime) {
        this.reachTime += reachTime;
        this.reachTimeCount++;
    }

    public int getPredecessorCount() {
        return predecessors;
    }

    public void setPredecessors(int predecessors) {
        this.predecessors += predecessors;
    }

    public int getSuccessorCount() {
        return successors;
    }

    public void setSuccessors(int successors) {
        this.successors += successors;
    }

    public void setContribution(int contribution) {
        this.contribution += contribution;
    }

    public int getContribution() {
        return this.contribution;
    }

}
