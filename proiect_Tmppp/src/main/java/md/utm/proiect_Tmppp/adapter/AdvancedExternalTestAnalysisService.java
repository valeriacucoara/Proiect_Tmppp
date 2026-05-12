package md.utm.proiect_Tmppp.adapter;

public class AdvancedExternalTestAnalysisService {

    public String processExamData(String formattedData) {
        // Different format expected
        if (formattedData.contains("SCORE=9") || formattedData.contains("SCORE=10")) {
            return "Advanced analysis: Exceptional candidate - immediate hire recommended";
        } else if (formattedData.contains("SCORE=7") || formattedData.contains("SCORE=8")) {
            return "Advanced analysis: Strong candidate - proceed to final interview";
        } else {
            return "Advanced analysis: Candidate needs improvement";
        }
    }
}