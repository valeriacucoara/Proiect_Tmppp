package md.utm.proiect_Tmppp.bridge;

public class DashboardSender implements ReportSender {

    @Override
    public void send(String message) {
        System.out.println("Afisat in DASHBOARD: " + message);
    }
}