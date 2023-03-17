package datamining.pattern;

import java.util.HashSet;
import java.util.Set;

public abstract class MyNotifyingPrefixer extends Thread {
    private final Set<MyPrefixerCompleteListener> listeners = new HashSet<MyPrefixerCompleteListener>();
    public final void addListener(final MyPrefixerCompleteListener listener) {
        listeners.add(listener);
    }
    public final void removeListener(final MyPrefixerCompleteListener listener) {
        listeners.remove(listener);
    }
    private final void notifyListeners() {
        for (MyPrefixerCompleteListener listener : listeners) {
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