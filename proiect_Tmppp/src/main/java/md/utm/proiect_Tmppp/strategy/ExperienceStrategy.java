package md.utm.proiect_Tmppp.strategy;

public class ExperienceStrategy implements CandidateEvaluationStrategy {

    @Override
    public String evaluate(String candidateName, int score, int experienceYears, int skillCount) {
        if (experienceYears >= 3) {
            return candidateName + " este acceptat pe baza experientei.";
        }
        return candidateName + " nu are suficienta experienta.";
    }
}