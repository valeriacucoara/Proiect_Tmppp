package md.utm.proiect_Tmppp.adapter;

import md.utm.proiect_Tmppp.entity.Candidate;

// Adapter: transforma evaluateCandidate() din platforma in analyzeExam() pentru serviciul extern.
public class TestEvaluationAdapter implements CandidateEvaluator, CandidateTestEvaluator {

    private ExternalEvaluationService adaptee;

    public TestEvaluationAdapter(ExternalEvaluationService adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String evaluateCandidate(Candidate candidate, int testScore) {
        String specialData = convertToServiceFormat(
                candidate.getName(),
                candidate.getAppliedJobTitle() == null ? "Technical Test" : candidate.getAppliedJobTitle(),
                testScore,
                candidate.getSkill()
        );
        return adaptee.analyzeExam(specialData);
    }

    @Override
    public String evaluateTest(String candidateName, String testName, int score) {
        String specialData = convertToServiceFormat(candidateName, testName, score, "N/A");
        return adaptee.analyzeExam(specialData);
    }

    private String convertToServiceFormat(String candidateName, String testName, int score, String skills) {
        String level;

        if (score >= 85) {
            level = "HIGH";
        } else if (score >= 60) {
            level = "MEDIUM";
        } else {
            level = "LOW";
        }

        return "CANDIDATE=" + candidateName +
                ";TEST=" + testName +
                ";SCORE=" + score +
                ";LEVEL=" + level +
                ";SKILLS=" + (skills == null || skills.isBlank() ? "N/A" : skills);
    }
}
