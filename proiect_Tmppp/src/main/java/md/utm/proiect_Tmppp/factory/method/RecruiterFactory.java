package md.utm.proiect_Tmppp.factory.method;

import md.utm.proiect_Tmppp.entity.Recruiter;
import md.utm.proiect_Tmppp.entity.User;

public class RecruiterFactory extends UserFactory {
    @Override
    public User createUser() {
        return new Recruiter();
    }
}