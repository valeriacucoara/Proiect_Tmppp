package md.utm.proiect_Tmppp.bridge;

public class DetailedTestReport extends TestReport {

    public DetailedTestReport(ReportSender sender) {
        super(sender);
    }

    @Override
    public void generateReport(String candidateName, int score) {

        String result;

        if (score >= 85) {
            result = "Excellent";
        } else if (score >= 60) {
            result = "Good";
        } else {
            result = "Needs improvement";
        }

        String message = "Raport candidat: " + candidateName +
                " | scor: " + score +
                " | evaluare: " + result;

        sender.send(message);
    }
}