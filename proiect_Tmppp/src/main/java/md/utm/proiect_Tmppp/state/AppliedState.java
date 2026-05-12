package md.utm.proiect_Tmppp.state;

public class AppliedState implements CandidateState {
    @Override
    public String handleStatus() {
        return "Candidatul a aplicat pentru job.";
    }

    @Override
    public String getStatusName() {
        return "Applied";
    }

    @Override
    public void interview(CandidateContext context) {
        context.setState(new InterviewedState());
        System.out.println("Candidate moved to Interviewed state.");
    }

    @Override
    public void accept(CandidateContext context) {
        // Cannot accept without interview
        System.out.println("Cannot accept candidate without interview.");
    }

    @Override
    public void reject(CandidateContext context) {
        context.setState(new RejectedState());
        System.out.println("Candidate rejected.");
    }
}