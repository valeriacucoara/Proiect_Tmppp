package md.utm.proiect_Tmppp.adapter;

// Adaptee: serviciu extern cu o metoda incompatibila fata de interfata platformei.
public class ExternalEvaluationService {

    public String analyzeExam(String examPayload) {
        int score = extractScore(examPayload);
        return score >= 70 ? "PASS" : "FAIL";
    }

    private int extractScore(String examPayload) {
        String marker = "SCORE=";
        int start = examPayload.indexOf(marker);
        if (start < 0) {
            return 0;
        }

        int valueStart = start + marker.length();
        int valueEnd = examPayload.indexOf(";", valueStart);
        String scoreText = valueEnd < 0
                ? examPayload.substring(valueStart)
                : examPayload.substring(valueStart, valueEnd);

        try {
            return Integer.parseInt(scoreText.trim());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
}
