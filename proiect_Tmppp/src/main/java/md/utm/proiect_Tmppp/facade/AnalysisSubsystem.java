package md.utm.proiect_Tmppp.facade;

// Subsystem: genereaza raportul final al procesului de recrutare.
public class AnalysisSubsystem {

    public String generateFinalReport(String candidateName, String jobTitle, int score) {
        String recommendation = score >= 70 ? "continue with interview" : "reject candidate";
        return "Final report generated: " + candidateName + " for " + jobTitle + " - recommendation: " + recommendation;
    }
}
