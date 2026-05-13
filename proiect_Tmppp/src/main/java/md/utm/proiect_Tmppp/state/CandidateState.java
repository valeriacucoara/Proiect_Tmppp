package md.utm.proiect_Tmppp.state;

// State: defineste comportamentul comun pentru fiecare status al candidatului.
public interface CandidateState {
    String handleStatus();
    String getStatusName();
    void interview(CandidateContext context);
    void accept(CandidateContext context);
    void reject(CandidateContext context);
}
