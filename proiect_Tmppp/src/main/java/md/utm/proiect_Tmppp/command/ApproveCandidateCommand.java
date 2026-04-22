package md.utm.proiect_Tmppp.command;

public class ApproveCandidateCommand implements Command {

    private CandidateReceiver receiver;
    private String candidateName;

    public ApproveCandidateCommand(CandidateReceiver receiver, String candidateName) {
        this.receiver = receiver;
        this.candidateName = candidateName;
    }

    @Override
    public String execute() {
        return receiver.approve(candidateName);
    }
}