package md.utm.proiect_Tmppp.factory.abstractfactory;

import md.utm.proiect_Tmppp.entity.*;

public class PremiumPlatformFactory implements RecruitmentAbstractFactory {

    @Override
    public User createUser() {
        // Could return a premium candidate with extra features
        return new Candidate();
    }

    @Override
    public JobListing createJobListing() {
        JobListing job = new JobListing();
        job.setTitle("Premium Job");
        job.setSalary(3000); // Higher salary
        return job;
    }

    @Override
    public Internship createInternship() {
        Internship internship = new Internship();
        // Premium internship with benefits
        return internship;
    }

    @Override
    public Test createTest() {
        Test test = new Test();
        // Advanced test
        return test;
    }

    @Override
    public Skill createSkill() {
        Skill skill = new Skill();
        // Premium skill validation
        return skill;
    }
}
