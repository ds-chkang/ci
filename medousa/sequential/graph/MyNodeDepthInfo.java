package medousa.sequential.graph;

import java.io.Serializable;

public class MyNodeDepthInfo
implements Serializable {

    private int contribution;
    private int inContribution;
    private int outContribution;
    private long duration;
    private long maxDuration;
    private long minDuration = 100000000000000L;
    private int durationCount;
    private long reachTime;
    private long maxReachTime;
    private long minReachTime = 100000000000000L;
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
        this.setMaxDuration(duration);
        this.setMinDuration(duration);
        this.durationCount++;
    }
    public void setMaxDuration(long duration) {
        if (this.maxDuration < duration) {
            this.maxDuration = duration;
        }
    }
    public long getMaxDuration() {return this.maxDuration;}
    public void setMinDuration(long duration) {
        if (duration > 0 && this.minDuration > duration) {
            this.minDuration = duration;
        }
    }
    public long getMinDuration() {
        if (this.minDuration == 100000000000000L) {
            return 0L;
        }
        return this.minDuration;
    }
    public void setMaxReachTime(long reachTime) {
        if (this.maxReachTime < reachTime) {
            this.maxReachTime = reachTime;
        }
    }
    public void setMinReachTime(long reachTime) {
        if (reachTime > 0 && this.minReachTime > reachTime) {
            this.minReachTime = reachTime;
        }
    }
    public long getMinReachTime() {
        if (this.minReachTime == 100000000000000L) {
            return 0L;
        }
        return this.minReachTime;
    }
    public long getMaxReachTime() {return this.maxReachTime;}
    public long getReachTime() {return reachTime;}
    public double getAverageReachTime() {
        if (this.reachTimeCount == 0) {return 0.00D;}
        return (double)this.reachTime/this.reachTimeCount;
    }
    public void setReachTime(long reachTime) {
        this.reachTime += reachTime;
        this.setMaxReachTime(reachTime);
        this.setMinReachTime(reachTime);
        this.reachTimeCount++;
    }
    public int getPredecessorCount() {return predecessors;}
    public void setPredecessors(int predecessors) {this.predecessors += predecessors;}
    public int getSuccessorCount() {return successors;}
    public void setSuccessors(int successors) {this.successors += successors;}
    public void setContribution(int contribution) {this.contribution += contribution;}
    public int getContribution() {return this.contribution;}
}
