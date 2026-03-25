package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.adapter.CandidateTestEvaluator;
import md.utm.proiect_Tmppp.adapter.ExternalTestAnalysisService;
import md.utm.proiect_Tmppp.adapter.TestEvaluationAdapter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adapter")
public class AdapterController {

    @GetMapping("/evaluate")
    public String testAdapter(
            @RequestParam String candidateName,
            @RequestParam String testName,
            @RequestParam int score
    ) {
        CandidateTestEvaluator evaluator =
                new TestEvaluationAdapter(new ExternalTestAnalysisService());

        return evaluator.evaluateTest(candidateName, testName, score);
    }
}