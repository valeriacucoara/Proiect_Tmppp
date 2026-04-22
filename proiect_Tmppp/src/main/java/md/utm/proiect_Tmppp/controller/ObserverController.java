package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.observer.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/observer")
public class ObserverController {

    @GetMapping("/notify")
    public String testObserver(@RequestParam String status) {
        JobApplicationSubject subject = new JobApplicationSubject();

        subject.attach(new CandidateObserver("Ana"));
        subject.attach(new RecruiterObserver("Maria"));

        return subject.setStatus(status);
    }
}