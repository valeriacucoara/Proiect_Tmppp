package md.utm.proiect_Tmppp.command;

import md.utm.proiect_Tmppp.entity.Candidate;

public class RejectCandidateCommand implements Command {

    private CandidateReceiver receiver;
    private String candidateName;
    private Candidate candidate;

    public RejectCandidateCommand(CandidateReceiver receiver, String candidateName) {
        this.receiver = receiver;
        this.candidateName = candidateName;
    }

    // Concrete Command: transforms the recruiter reject action into an executable object.
    public RejectCandidateCommand(CandidateReceiver receiver, Candidate candidate) {
        this.receiver = receiver;
        this.candidate = candidate;
    }

    @Override
    public String execute() {
        if (candidate != null) {
            return receiver.reject(candidate);
        }
        return receiver.reject(candidateName);
    }
}
