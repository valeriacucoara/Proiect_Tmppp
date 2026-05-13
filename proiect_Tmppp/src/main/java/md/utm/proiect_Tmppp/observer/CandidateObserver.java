package md.utm.proiect_Tmppp.observer;

// Concrete Observer: reprezinta candidatul care primeste notificari despre aplicatia sa.
public class CandidateObserver implements Observer {

    private String name;

    public CandidateObserver(String name) {
        this.name = name;
    }

    @Override
    public String update(String message) {
        return message;
    }
}
