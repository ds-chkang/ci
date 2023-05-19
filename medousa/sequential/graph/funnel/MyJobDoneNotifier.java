package medousa.sequential.graph.funnel;

import java.util.HashSet;
import java.util.Set;

public abstract class MyJobDoneNotifier
extends Thread {
    private final Set<MyJobCompleteListener> jobs = new HashSet<>();
    public final void addJob(final MyJobCompleteListener listener) {
        jobs.add(listener);
    }
    public final void removeJob(final MyJobCompleteListener listener) {
        jobs.remove(listener);
    }
    private final void notifyJobs() {
        for (MyJobCompleteListener listener : jobs) {
            listener.startNextJob(this);
        }
    }
    @Override public final void run() {
        try {
            doRun();
        } finally {
            notifyJobs();
        }
    }
    public abstract void doRun();
}