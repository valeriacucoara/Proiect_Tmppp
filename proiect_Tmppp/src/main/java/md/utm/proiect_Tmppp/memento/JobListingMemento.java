package md.utm.proiect_Tmppp.memento;

public class JobListingMemento {

    private final String title;
    private final String description;

    public JobListingMemento(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}