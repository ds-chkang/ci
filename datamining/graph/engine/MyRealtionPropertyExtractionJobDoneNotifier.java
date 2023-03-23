package datamining.graph.engine;

import java.util.HashSet;
import java.util.Set;

public abstract class MyRealtionPropertyExtractionJobDoneNotifier extends Thread {
    private final Set<MyRelationExtractionJobCompleteListener> listeners = new HashSet<>();
    public final void addListener(final MyRelationExtractionJobCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final MyRelationExtractionJobCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (MyRelationExtractionJobCompleteListener listener : listeners) {
            listener.startNextPrefixer(this);
        }
    }
    @Override
    public final void run() {
        try {
            doRun();
        } finally {
            notifyListeners();
        }
    }
    public abstract void doRun();
}