package md.utm.proiect_Tmppp.strategy;

import md.utm.proiect_Tmppp.entity.Candidate;

// Strategy: defineste algoritmul comun pentru evaluarea candidatului.
public interface EvaluationStrategy {

    String evaluate(Candidate candidate, int testScore, int experienceYears, String recruiterNotes);
}
