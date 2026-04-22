package md.utm.proiect_Tmppp.command;

public class RecruiterInvoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public String runCommand() {
        return command.execute();
    }
}