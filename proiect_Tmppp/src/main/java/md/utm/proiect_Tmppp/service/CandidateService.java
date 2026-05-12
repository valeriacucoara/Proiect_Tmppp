package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.Candidate;
import java.util.List;

public interface CandidateService {
    List<Candidate> getAllCandidates();
    Candidate saveCandidate(Candidate candidate);
    void deleteCandidate(Long id);
}