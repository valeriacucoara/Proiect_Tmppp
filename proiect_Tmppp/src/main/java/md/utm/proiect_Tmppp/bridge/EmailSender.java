package md.utm.proiect_Tmppp.bridge;

public class EmailSender implements ReportSender {

    @Override
    public void send(String message) {
        System.out.println("Trimis prin EMAIL: " + message);
    }
}