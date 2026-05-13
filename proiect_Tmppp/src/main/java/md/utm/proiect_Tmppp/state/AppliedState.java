package md.utm.proiect_Tmppp.state;

// Concrete State: reprezinta candidatul care a aplicat si asteapta evaluarea recruiterului.
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
        context.setState(new AcceptedState());
        System.out.println("Candidate accepted directly from Applied state.");
    }

    @Override
    public void reject(CandidateContext context) {
        context.setState(new RejectedState());
        System.out.println("Candidate rejected.");
    }
}
