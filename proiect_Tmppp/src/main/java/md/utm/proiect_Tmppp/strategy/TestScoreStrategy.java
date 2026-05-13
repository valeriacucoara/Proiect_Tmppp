package md.utm.proiect_Tmppp.strategy;

import md.utm.proiect_Tmppp.entity.Candidate;

// Concrete Strategy: evalueaza candidatul pe baza scorului obtinut la test.
public class TestScoreStrategy implements EvaluationStrategy {

    @Override
    public String evaluate(Candidate candidate, int testScore, int experienceYears, String recruiterNotes) {
        String result = testScore >= 70 ? "PASS" : "FAIL";
        return "Evaluation method: Test Score. Result: " + result + ". Test score: " + testScore;
    }
}
