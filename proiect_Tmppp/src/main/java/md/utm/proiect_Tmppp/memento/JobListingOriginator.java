package md.utm.proiect_Tmppp.memento;

public class JobListingOriginator {

    private String title;
    private String description;

    public void setState(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String showState() {
        return "Titlu: " + title + " | Descriere: " + description;
    }

    public JobListingMemento save() {
        return new JobListingMemento(title, description);
    }

    public void restore(JobListingMemento memento) {
        this.title = memento.getTitle();
        this.description = memento.getDescription();
    }
}