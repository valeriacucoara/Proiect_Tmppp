package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.facade.RecruitmentFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facade")
public class FacadeController {

    @GetMapping("/recruitment")
    public String testFacade(
            @RequestParam String candidateName,
            @RequestParam String jobTitle,
            @RequestParam String skillName,
            @RequestParam String testName
    ) {
        RecruitmentFacade facade = new RecruitmentFacade();
        return facade.subsystemOperation(candidateName, jobTitle, skillName, testName);
    }
}