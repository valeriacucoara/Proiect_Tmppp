package md.utm.proiect_Tmppp.facade;

public class RecruitmentFacade {

    private CandidateSubsystem candidateSubsystem;
    private JobSubsystem jobSubsystem;
    private SkillSubsystem skillSubsystem;
    private TestSubsystem testSubsystem;
    private AdditionalRecruitmentFacade optionalAdditionalFacade;

    public RecruitmentFacade() {
        this.candidateSubsystem = new CandidateSubsystem();
        this.jobSubsystem = new JobSubsystem();
        this.skillSubsystem = new SkillSubsystem();
        this.testSubsystem = new TestSubsystem();
        this.optionalAdditionalFacade = new AdditionalRecruitmentFacade();
    }

    public String subsystemOperation(String candidateName, String jobTitle, String skillName, String testName) {
        StringBuilder result = new StringBuilder();

        result.append(candidateSubsystem.createCandidate(candidateName)).append("\n");
        result.append(jobSubsystem.assignJob(jobTitle)).append("\n");
        result.append(skillSubsystem.addSkill(skillName)).append("\n");
        result.append(testSubsystem.registerTest(testName)).append("\n");
        result.append(optionalAdditionalFacade.anotherOperation(candidateName));

        return result.toString();
    }
}