package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.decorator.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/decorator")
public class DecoratorController {

    @GetMapping("/job")
    public String testDecorator() {
        JobDisplay job = new BasicJobDisplay("Java Developer", "Endava", 2500);

        job = new UrgentJobDecorator(job);
        job = new FeaturedJobDecorator(job);

        return job.showJob();
    }
}