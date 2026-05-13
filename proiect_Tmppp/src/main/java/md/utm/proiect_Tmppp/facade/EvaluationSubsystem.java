package md.utm.proiect_Tmppp.facade;

// Subsystem: evalueaza testul candidatului.
public class EvaluationSubsystem {

    public String evaluateTest(String candidateName, int score) {
        String result = score >= 70 ? "PASS" : "FAIL";
        return "Test evaluated: " + candidateName + " - " + result;
    }
}
