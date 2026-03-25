package md.utm.proiect_Tmppp.adapter;

public class ExternalTestAnalysisService {

    public String analyzeExam(String specialData) {
        if (specialData.contains("LEVEL=HIGH")) {
            return "Rezultat extern: candidat recomandat pentru interviu tehnic";
        } else if (specialData.contains("LEVEL=MEDIUM")) {
            return "Rezultat extern: candidat acceptat conditionat";
        } else {
            return "Rezultat extern: candidat respins";
        }
    }
}