package md.utm.proiect_Tmppp.factory.method;

import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.User;

// Concrete Creator: creeaza produsul concret Candidate.
public class CandidateFactory extends UserFactory {
    @Override
    public User createUser() {
        return new Candidate();
    }
}
