package md.utm.proiect_Tmppp.state;

public interface CandidateState {
    String handleStatus();
    String getStatusName();
    void interview(CandidateContext context);
    void accept(CandidateContext context);
    void reject(CandidateContext context);
}