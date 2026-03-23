package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.entity.JobListing;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prototype")
public class PrototypeController {

    @GetMapping("/clone")
    public JobListing cloneJob() {

        JobListing original = new JobListing();
        original.setTitle("Java Developer");
        original.setDescription("Spring Boot job");
        original.setSalary(1500);

        JobListing copy = original.clone();

        return copy;
    }
}