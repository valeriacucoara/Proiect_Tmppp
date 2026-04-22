package md.utm.proiect_Tmppp.observer;

public class RecruiterObserver implements Observer {

    private String name;

    public RecruiterObserver(String name) {
        this.name = name;
    }

    @Override
    public String update(String message) {
        return "Recruiter " + name + " notificat: " + message;
    }
}