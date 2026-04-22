package md.utm.proiect_Tmppp.strategy;

public class CandidateEvaluator {

    private CandidateEvaluationStrategy strategy;

    public void setStrategy(CandidateEvaluationStrategy strategy) {
        this.strategy = strategy;
    }

    public String evaluateCandidate(String candidateName, int score, int experienceYears, int skillCount) {
        return strategy.evaluate(candidateName, score, experienceYears, skillCount);
    }
}