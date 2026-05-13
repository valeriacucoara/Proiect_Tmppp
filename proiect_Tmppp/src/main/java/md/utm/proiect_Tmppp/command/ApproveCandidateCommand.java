package md.utm.proiect_Tmppp.command;

import md.utm.proiect_Tmppp.entity.Candidate;

public class ApproveCandidateCommand implements Command {

    private CandidateReceiver receiver;
    private String candidateName;
    private Candidate candidate;

    public ApproveCandidateCommand(CandidateReceiver receiver, String candidateName) {
        this.receiver = receiver;
        this.candidateName = candidateName;
    }

    // Concrete Command: transforms the recruiter accept action into an executable object.
    public ApproveCandidateCommand(CandidateReceiver receiver, Candidate candidate) {
        this.receiver = receiver;
        this.candidate = candidate;
    }

    @Override
    public String execute() {
        if (candidate != null) {
            return receiver.approve(candidate);
        }
        return receiver.approve(candidateName);
    }
}
