package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.strategy.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/strategy")
public class StrategyController {

    @GetMapping("/evaluate")
    public String testStrategy(
            @RequestParam String type,
            @RequestParam String candidateName,
            @RequestParam int score,
            @RequestParam int experienceYears,
            @RequestParam int skillCount
    ) {
        CandidateEvaluator evaluator = new CandidateEvaluator();

        if (type.equalsIgnoreCase("score")) {
            evaluator.setStrategy(new ScoreStrategy());
        } else if (type.equalsIgnoreCase("experience")) {
            evaluator.setStrategy(new ExperienceStrategy());
        } else {
            evaluator.setStrategy(new SkillStrategy());
        }

        return evaluator.evaluateCandidate(candidateName, score, experienceYears, skillCount);
    }
}