package md.utm.proiect_Tmppp.adapter;

import md.utm.proiect_Tmppp.entity.Candidate;

// Target: interfata interna folosita de platforma pentru evaluarea candidatilor.
public interface CandidateEvaluator {
    String evaluateCandidate(Candidate candidate, int testScore);
}
