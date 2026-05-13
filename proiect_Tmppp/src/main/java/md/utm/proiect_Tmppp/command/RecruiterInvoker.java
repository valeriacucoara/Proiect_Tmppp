package md.utm.proiect_Tmppp.command;

// Invoker: receives a command from the recruiter flow and triggers its execution.
public class RecruiterInvoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public String runCommand() {
        return command.execute();
    }
}
