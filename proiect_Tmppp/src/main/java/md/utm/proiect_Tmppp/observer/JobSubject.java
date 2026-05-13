package md.utm.proiect_Tmppp.observer;

import java.util.ArrayList;
import java.util.List;

// Subject: pastreaza lista de observeri si trimite notificari cand apar schimbari in recrutare.
public class JobSubject {

    private final List<Observer> observers = new ArrayList<>();

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public List<String> notifyObservers(String message) {
        List<String> notifications = new ArrayList<>();
        for (Observer observer : observers) {
            notifications.add(observer.update(message));
        }
        return notifications;
    }
}
