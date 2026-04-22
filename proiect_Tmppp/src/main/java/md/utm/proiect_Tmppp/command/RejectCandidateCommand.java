package md.utm.proiect_Tmppp.command;

public class RejectCandidateCommand implements Command {

    private CandidateReceiver receiver;
    private String candidateName;

    public RejectCandidateCommand(CandidateReceiver receiver, String candidateName) {
        this.receiver = receiver;
        this.candidateName = candidateName;
    }

    @Override
    public String execute() {
        return receiver.reject(candidateName);
    }
}