package md.utm.proiect_Tmppp.factory.abstractfactory;

import md.utm.proiect_Tmppp.entity.*;

public class AbstractFactoryTest {
    public static void main(String[] args) {
        RecruitmentAbstractFactory candidateFactory = new CandidatePlatformFactory();
        User candidate = candidateFactory.createUser();
        candidate.showRole();
        JobListing jobListing = candidateFactory.createJobListing();
        System.out.println("JobListing creat pentru candidat: " + jobListing.getTitle());

        RecruitmentAbstractFactory recruiterFactory = new RecruiterPlatformFactory();
        User recruiter = recruiterFactory.createUser();
        recruiter.showRole();
        JobListing recruiterJob = recruiterFactory.createJobListing();
        System.out.println("JobListing creat pentru recruiter: " + recruiterJob.getTitle());
    }
}