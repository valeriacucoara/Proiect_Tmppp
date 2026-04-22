package md.utm.proiect_Tmppp.memento;

import java.util.ArrayList;
import java.util.List;

public class JobListingHistory {

    private List<JobListingMemento> history = new ArrayList<>();

    public void addMemento(JobListingMemento memento) {
        history.add(memento);
    }

    public JobListingMemento getMemento(int index) {
        return history.get(index);
    }
}