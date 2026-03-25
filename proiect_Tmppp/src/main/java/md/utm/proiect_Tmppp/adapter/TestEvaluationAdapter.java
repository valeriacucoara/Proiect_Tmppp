package md.utm.proiect_Tmppp.adapter;

public class TestEvaluationAdapter implements CandidateTestEvaluator {

    private ExternalTestAnalysisService adaptee;

    public TestEvaluationAdapter(ExternalTestAnalysisService adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String evaluateTest(String candidateName, String testName, int score) {
        String specialData = convertToServiceFormat(candidateName, testName, score);
        return adaptee.analyzeExam(specialData);
    }

    private String convertToServiceFormat(String candidateName, String testName, int score) {
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
                ";LEVEL=" + level;
    }
}