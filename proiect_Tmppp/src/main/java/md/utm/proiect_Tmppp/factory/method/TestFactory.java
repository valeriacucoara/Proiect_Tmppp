package md.utm.proiect_Tmppp.factory.method;

import md.utm.proiect_Tmppp.entity.User;

public class TestFactory {
    public static void main(String[] args) {
        // Factory Method
        UserFactory candidateFactory = new CandidateFactory();
        User candidate = candidateFactory.createUser();
        candidate.showRole();

        UserFactory recruiterFactory = new RecruiterFactory();
        User recruiter = recruiterFactory.createUser();
        recruiter.showRole();
    }
}