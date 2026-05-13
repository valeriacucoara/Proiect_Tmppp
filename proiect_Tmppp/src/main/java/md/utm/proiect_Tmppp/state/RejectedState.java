package md.utm.proiect_Tmppp.state;

// Concrete State: reprezinta candidatul respins in procesul de recrutare.
public class RejectedState implements CandidateState {
    @Override
    public String handleStatus() {
        return "Candidatul a fost respins.";
    }

    @Override
    public String getStatusName() {
        return "Rejected";
    }

    @Override
    public void interview(CandidateContext context) {
        // Cannot interview rejected
        System.out.println("Cannot interview rejected candidate.");
    }

    @Override
    public void accept(CandidateContext context) {
        context.setState(new AcceptedState());
        System.out.println("Rejected candidate moved to Accepted state.");
    }

    @Override
    public void reject(CandidateContext context) {
        // Already rejected
        System.out.println("Candidate already rejected.");
    }
}
