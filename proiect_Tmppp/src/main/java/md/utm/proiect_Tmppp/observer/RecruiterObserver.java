package md.utm.proiect_Tmppp.observer;

// Concrete Observer: reprezinta recruiterul care primeste notificari despre candidati si joburi.
public class RecruiterObserver implements Observer {

    private String name;

    public RecruiterObserver(String name) {
        this.name = name;
    }

    @Override
    public String update(String message) {
        return message;
    }
}
