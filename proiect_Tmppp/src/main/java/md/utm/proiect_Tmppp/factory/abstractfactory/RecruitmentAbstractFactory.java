package md.utm.proiect_Tmppp.factory.abstractfactory;

import md.utm.proiect_Tmppp.entity.*;

public interface RecruitmentAbstractFactory {
    User createUser();
    JobListing createJobListing();
    Internship createInternship();
    Test createTest();
    Skill createSkill();
}