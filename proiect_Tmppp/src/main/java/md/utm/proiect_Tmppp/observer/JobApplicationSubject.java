package md.utm.proiect_Tmppp.observer;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationSubject {

    private List<Observer> observers = new ArrayList<>();
    private String status;

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public String setStatus(String status) {
        this.status = status;
        return notifyObservers();
    }

    public String notifyObservers() {
        StringBuilder result = new StringBuilder();
        for (Observer observer : observers) {
            result.append(observer.update("Status aplicatie schimbat: " + status)).append("\n");
        }
        return result.toString();
    }
}