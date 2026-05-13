package md.utm.proiect_Tmppp.state;

// Concrete State: reprezinta candidatul evaluat/intervievat, pregatit pentru decizie.
public class InterviewedState implements CandidateState {
    @Override
    public String handleStatus() {
        return "Candidatul a trecut prin interviu.";
    }

    @Override
    public String getStatusName() {
        return "Interviewed";
    }

    @Override
    public void interview(CandidateContext context) {
        // Already interviewed
        System.out.println("Candidate already interviewed.");
    }

    @Override
    public void accept(CandidateContext context) {
        context.setState(new AcceptedState());
        System.out.println("Candidate accepted.");
    }

    @Override
    public void reject(CandidateContext context) {
        context.setState(new RejectedState());
        System.out.println("Candidate rejected after interview.");
    }
}
