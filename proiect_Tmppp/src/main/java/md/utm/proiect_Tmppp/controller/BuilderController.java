package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.builder.*;
import md.utm.proiect_Tmppp.entity.JobListing;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/builder")
public class BuilderController {

    @GetMapping("/{type}")
    public JobListing createJob(@PathVariable String type) {

        JobBuilder builder = new JobListingBuilder();
        JobDirector director = new JobDirector(builder);

        return director.make(type);
    }
}