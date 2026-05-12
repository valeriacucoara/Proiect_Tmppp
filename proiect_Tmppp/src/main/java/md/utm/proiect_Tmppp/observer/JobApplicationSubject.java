package md.utm.proiect_Tmppp.observer;

import java.util.ArrayList;
import java.util.List;

public class JobApplicationSubject {

    private List<Observer> observers = new ArrayList<>();
    private String status;
    private String applicantName;
    private String jobTitle;

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void setApplicationDetails(String applicantName, String jobTitle) {
        this.applicantName = applicantName;
        this.jobTitle = jobTitle;
    }

    public String setStatus(String status) {
        this.status = status;
        return notifyObservers();
    }

    public String updateStatus(String status) {
        return setStatus(status);
    }

    public String notifyObservers() {
        StringBuilder result = new StringBuilder();
        String message = "Aplicant: " + (applicantName == null ? "necunoscut" : applicantName)
                + ", Job: " + (jobTitle == null ? "necunoscut" : jobTitle)
                + ", Status: " + status;
        for (Observer observer : observers) {
            result.append(observer.update(message)).append("\n");
        }
        return result.toString();
    }
}
