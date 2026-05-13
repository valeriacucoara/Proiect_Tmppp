package md.utm.proiect_Tmppp.strategy;

import md.utm.proiect_Tmppp.entity.Candidate;

// Concrete Strategy: evalueaza candidatul pe baza anilor de experienta.
public class ExperienceStrategy implements CandidateEvaluationStrategy, EvaluationStrategy {

    @Override
    public String evaluate(String candidateName, int score, int experienceYears, int skillCount) {
        if (experienceYears >= 3) {
            return candidateName + " este acceptat pe baza experientei.";
        }
        return candidateName + " nu are suficienta experienta.";
    }

    @Override
    public String evaluate(Candidate candidate, int testScore, int experienceYears, String recruiterNotes) {
        String result = experienceYears >= 3 ? "PASS" : "FAIL";
        return "Evaluation method: Experience Evaluation. Result: " + result + ". Experience years: " + experienceYears;
    }
}
