package md.utm.proiect_Tmppp.state;

public class AcceptedState implements CandidateState {
    @Override
    public String handleStatus() {
        return "Candidatul a fost acceptat.";
    }

    @Override
    public String getStatusName() {
        return "Accepted";
    }

    @Override
    public void interview(CandidateContext context) {
        // Already accepted
        System.out.println("Candidate already accepted.");
    }

    @Override
    public void accept(CandidateContext context) {
        // Already accepted
        System.out.println("Candidate already accepted.");
    }

    @Override
    public void reject(CandidateContext context) {
        // Cannot reject after accept
        System.out.println("Cannot reject accepted candidate.");
    }
}