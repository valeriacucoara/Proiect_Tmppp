package md.utm.proiect_Tmppp.observer;

public class CandidateObserver implements Observer {

    private String name;

    public CandidateObserver(String name) {
        this.name = name;
    }

    @Override
    public String update(String message) {
        return "Candidat " + name + " notificat: " + message;
    }
}