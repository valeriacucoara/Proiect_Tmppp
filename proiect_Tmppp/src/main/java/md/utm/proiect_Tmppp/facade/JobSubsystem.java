package md.utm.proiect_Tmppp.facade;

// Subsystem: gestioneaza analiza jobului asociat candidatului.
public class JobSubsystem {

    public String assignJob(String jobTitle) {
        return "Job asignat: " + jobTitle;
    }

    public String analyzeJob(String jobTitle) {
        return "Job analyzed: " + jobTitle;
    }
}
