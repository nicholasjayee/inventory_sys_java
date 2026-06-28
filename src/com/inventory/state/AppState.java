package com.inventory.state;

import com.inventory.models.User;
import java.util.ArrayList;
import java.util.List;

public class AppState {
    private static AppState instance;
    private User currentUser;
    private final List<StateChangeListener> listeners = new ArrayList<>();

    public interface StateChangeListener {
        void onStateChanged(AppState state);
    }

    private AppState() {}

    public static synchronized AppState getInstance() {
        if (instance == null) {
            instance = new AppState();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        notifyListeners();
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void addListener(StateChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(StateChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (StateChangeListener listener : listeners) {
            listener.onStateChanged(this);
        }
    }
}
