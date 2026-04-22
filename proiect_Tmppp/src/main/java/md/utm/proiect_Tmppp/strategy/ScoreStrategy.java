package md.utm.proiect_Tmppp.strategy;

public class ScoreStrategy implements CandidateEvaluationStrategy {

    @Override
    public String evaluate(String candidateName, int score, int experienceYears, int skillCount) {
        if (score >= 85) {
            return candidateName + " este acceptat pe baza scorului mare.";
        }
        return candidateName + " nu a obtinut un scor suficient.";
    }
}