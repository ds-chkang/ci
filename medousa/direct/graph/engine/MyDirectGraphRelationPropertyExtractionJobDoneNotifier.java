package medousa.direct.graph.engine;

import java.util.HashSet;
import java.util.Set;

public abstract class MyDirectGraphRelationPropertyExtractionJobDoneNotifier extends Thread {
    private final Set<MyDirectGraphRelationExtractionJobCompleteListener> listeners = new HashSet<>();
    public final void addListener(final MyDirectGraphRelationExtractionJobCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final MyDirectGraphRelationExtractionJobCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (MyDirectGraphRelationExtractionJobCompleteListener listener : listeners) {
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