package md.utm.proiect_Tmppp.strategy;

import md.utm.proiect_Tmppp.entity.Candidate;

// Concrete Strategy: evalueaza candidatul pe baza continutului CV-ului si a skill-urilor.
public class CVStrategy implements EvaluationStrategy {

    @Override
    public String evaluate(Candidate candidate, int testScore, int experienceYears, String recruiterNotes) {
        String cv = candidate.getCv() == null ? "" : candidate.getCv().trim();
        String skills = candidate.getSkill() == null ? "" : candidate.getSkill().trim();
        boolean hasUsefulCv = !cv.isEmpty() && !"CV not provided yet.".equalsIgnoreCase(cv);
        boolean hasSkills = !skills.isEmpty() && !"N/A".equalsIgnoreCase(skills);
        String result = hasUsefulCv || hasSkills ? "PASS" : "FAIL";
        return "Evaluation method: CV Analysis. Result: " + result + ". CV and skills reviewed";
    }
}
