package md.utm.proiect_Tmppp.factory.abstractfactory;

import md.utm.proiect_Tmppp.entity.*;

public class CandidatePlatformFactory implements RecruitmentAbstractFactory {

    @Override
    public User createUser() { return new Candidate(); }

    @Override
    public JobListing createJobListing() { return new JobListing(); }

    @Override
    public Internship createInternship() { return new Internship(); }

    @Override
    public Test createTest() { return new Test(); }

    @Override
    public Skill createSkill() { return new Skill(); }
}