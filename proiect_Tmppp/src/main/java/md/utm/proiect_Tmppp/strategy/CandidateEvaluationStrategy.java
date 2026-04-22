package md.utm.proiect_Tmppp.strategy;

public interface CandidateEvaluationStrategy {
    String evaluate(String candidateName, int score, int experienceYears, int skillCount);
}