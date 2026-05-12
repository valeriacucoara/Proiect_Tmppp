package md.utm.proiect_Tmppp.state;

import md.utm.proiect_Tmppp.entity.Candidate;

public class CandidateContext {
    private CandidateState state;
    private Candidate candidate;

    public CandidateContext(Candidate candidate) {
        this.candidate = candidate;
        String status = candidate.getStatus();
        if (status == null || status.isBlank()) {
            this.state = new AppliedState();
        } else {
            switch (status) {
                case "Interviewed" -> this.state = new InterviewedState();
                case "Accepted" -> this.state = new AcceptedState();
                case "Rejected" -> this.state = new RejectedState();
                default -> this.state = new AppliedState();
            }
        }
        candidate.setState(this.state);
    }

    public void setState(CandidateState state) {
        this.state = state;
        candidate.setState(state);
    }

    public String request() {
        return state.handleStatus();
    }

    public String getCurrentStatus() {
        return state.getStatusName();
    }

    public void interview() {
        state.interview(this);
    }

    public void accept() {
        state.accept(this);
    }

    public void reject() {
        state.reject(this);
    }

    public Candidate getCandidate() {
        return candidate;
    }
}