package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.factory.method.*;
import md.utm.proiect_Tmppp.factory.abstractfactory.*;
import md.utm.proiect_Tmppp.entity.*;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/factory")
public class FactoryController {

    // Factory Method
    @GetMapping("/create-user/{type}")
    public String createUser(@PathVariable String type) {
        UserFactory factory;

        if (type.equalsIgnoreCase("candidate")) {
            factory = new CandidateFactory();
        } else {
            factory = new RecruiterFactory();
        }

        User user = factory.createUser();
        return "User creat: " + user.getClass().getSimpleName();
    }

    // Abstract Factory
    @GetMapping("/abstract/{type}")
    public String abstractFactory(@PathVariable String type) {
        RecruitmentAbstractFactory factory;

        if (type.equalsIgnoreCase("candidate")) {
            factory = new CandidatePlatformFactory();
        } else {
            factory = new RecruiterPlatformFactory();
        }

        User user = factory.createUser();
        JobListing job = factory.createJobListing();

        return "User: " + user.getClass().getSimpleName() +
                " | JobListing creat";
    }
}