package md.utm.proiect_Tmppp.strategy;

import md.utm.proiect_Tmppp.entity.Candidate;

// Context: primeste strategia aleasa si executa evaluarea fara sa cunoasca algoritmul concret.
public class CandidateEvaluationContext {

    private EvaluationStrategy strategy;

    public void setStrategy(EvaluationStrategy strategy) {
        this.strategy = strategy;
    }

    public String evaluateCandidate(Candidate candidate, int testScore, int experienceYears, String recruiterNotes) {
        if (strategy == null) {
            throw new IllegalStateException("Evaluation strategy was not selected");
        }
        return strategy.evaluate(candidate, testScore, experienceYears, recruiterNotes);
    }
}
