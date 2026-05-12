package md.utm.proiect_Tmppp.adapter;

public class AdvancedTestEvaluationAdapter implements CandidateTestEvaluator {

    private AdvancedExternalTestAnalysisService adaptee;

    public AdvancedTestEvaluationAdapter(AdvancedExternalTestAnalysisService adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public String evaluateTest(String candidateName, String testName, int score) {
        String formattedData = formatForAdvancedService(candidateName, testName, score);
        return adaptee.processExamData(formattedData);
    }

    private String formatForAdvancedService(String candidateName, String testName, int score) {
        return "CANDIDATE:" + candidateName + "|TEST:" + testName + "|SCORE:" + score;
    }
}