package md.utm.proiect_Tmppp.strategy;

public class SkillStrategy implements CandidateEvaluationStrategy {

    @Override
    public String evaluate(String candidateName, int score, int experienceYears, int skillCount) {
        if (skillCount >= 4) {
            return candidateName + " este acceptat pe baza numarului de skill-uri.";
        }
        return candidateName + " nu are suficiente skill-uri.";
    }
}