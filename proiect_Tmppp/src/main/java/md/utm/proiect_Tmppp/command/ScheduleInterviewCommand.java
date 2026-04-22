package md.utm.proiect_Tmppp.command;

public class ScheduleInterviewCommand implements Command {

    private CandidateReceiver receiver;
    private String candidateName;

    public ScheduleInterviewCommand(CandidateReceiver receiver, String candidateName) {
        this.receiver = receiver;
        this.candidateName = candidateName;
    }

    @Override
    public String execute() {
        return receiver.scheduleInterview(candidateName);
    }
}