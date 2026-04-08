package md.utm.proiect_Tmppp.bridge;

public abstract class TestReport {

    protected ReportSender sender;

    public TestReport(ReportSender sender) {
        this.sender = sender;
    }

    public abstract void generateReport(String candidateName, int score);
}