package md.utm.proiect_Tmppp.command;

public class CandidateReceiver {

    public String approve(String candidateName) {
        return "Candidatul " + candidateName + " a fost aprobat.";
    }

    public String reject(String candidateName) {
        return "Candidatul " + candidateName + " a fost respins.";
    }

    public String scheduleInterview(String candidateName) {
        return "Interviul pentru " + candidateName + " a fost programat.";
    }
}