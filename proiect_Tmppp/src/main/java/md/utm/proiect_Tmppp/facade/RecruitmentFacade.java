package md.utm.proiect_Tmppp.facade;

import java.util.ArrayList;
import java.util.List;

// Facade: ofera o singura metoda centrala pentru procesul complet de recrutare.
public class RecruitmentFacade {

    private CandidateSubsystem candidateSubsystem;
    private JobSubsystem jobSubsystem;
    private EvaluationSubsystem evaluationSubsystem;
    private AnalysisSubsystem analysisSubsystem;

    public RecruitmentFacade() {
        this.candidateSubsystem = new CandidateSubsystem();
        this.jobSubsystem = new JobSubsystem();
        this.evaluationSubsystem = new EvaluationSubsystem();
        this.analysisSubsystem = new AnalysisSubsystem();
    }

    public String subsystemOperation(String candidateName, String jobTitle, String skillName, String testName) {
        return String.join("\n", recruitmentProcess(candidateName, jobTitle, 85));
    }

    public List<String> recruitmentProcess(String candidateName, String jobTitle, int testScore) {
        List<String> result = new ArrayList<>();
        result.add(candidateSubsystem.verifyCandidate(candidateName));
        result.add(jobSubsystem.analyzeJob(jobTitle));
        result.add(evaluationSubsystem.evaluateTest(candidateName, testScore));
        result.add(analysisSubsystem.generateFinalReport(candidateName, jobTitle, testScore));
        result.add("Recruitment process completed successfully");

        return result;
    }
}
