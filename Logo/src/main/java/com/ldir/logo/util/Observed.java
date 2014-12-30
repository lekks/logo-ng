package com.ldir.logo.util;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ldir on 30.12.2014.
 */
public class Observed {
    public static class Value<T> extends Observable implements Observer {

        public void update(T value) {
            setChanged();
            notifyObservers(value);
        }

        @Override
        public void update(Observable o, Object arg) {
            setChanged();
            notifyObservers(arg);
        }
    }

    public static class ChangeFiltered<T> extends Observable implements Observer {
        private volatile T last = null;

        public ChangeFiltered(Observable source) {
            source.addObserver(this);
        }

        public ChangeFiltered() {
            super();
        }

        public void update(T value) {
            if ((last == null && value != null) || !last.equals(value)) {
                setChanged();
                notifyObservers(value);
                last = value;
            }
        }

        @Override
        public void update(Observable o, Object arg) {
            update((T)arg);
        }
    }

}
