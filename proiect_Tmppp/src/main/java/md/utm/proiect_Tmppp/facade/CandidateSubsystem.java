package md.utm.proiect_Tmppp.facade;

// Subsystem: gestioneaza verificarea candidatului.
public class CandidateSubsystem {

    public String createCandidate(String candidateName) {
        return "Candidat creat: " + candidateName;
    }

    public String verifyCandidate(String candidateName) {
        return "Candidate verified: " + candidateName;
    }
}
