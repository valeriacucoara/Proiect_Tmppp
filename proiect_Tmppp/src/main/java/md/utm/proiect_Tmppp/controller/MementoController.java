package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.memento.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/memento")
public class MementoController {

    @GetMapping("/job")
    public String testMemento() {
        JobListingOriginator job = new JobListingOriginator();
        JobListingHistory history = new JobListingHistory();

        job.setState("Java Developer", "Initial job");
        history.addMemento(job.save());

        job.setState("Senior Java Developer", "Updated job");
        history.addMemento(job.save());

//        job.restore(history.getMemento(0));

        return job.showState();
    }
}