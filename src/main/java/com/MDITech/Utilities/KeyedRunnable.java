package com.MDITech.Utilities;

public abstract class KeyedRunnable<T> implements Runnable {

    public final T key;

    public KeyedRunnable(T key) {
        this.key = key;
    }

    public String toDisplay() {
        return key != null ? key.toString() : "<Unassigned Key Value>";
    }

    public static KeyedRunnable of(Object key,Runnable runnable) {
        return new KeyedRunnable (key) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

}
