package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.facade.RecruitmentFacade;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.state.CandidateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentProcessService {

    @Autowired
    private CandidateRepository candidateRepository;

    public List<String> processCandidate(Long candidateId, int testScore) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        String jobTitle = candidate.getAppliedJobTitle() == null || candidate.getAppliedJobTitle().isBlank()
                ? "General recruitment"
                : candidate.getAppliedJobTitle();

        RecruitmentFacade facade = new RecruitmentFacade();
        List<String> result = facade.recruitmentProcess(candidate.getName(), jobTitle, testScore);

        candidate.setRecruiterMessage(String.join("\n", result));
        CandidateContext stateContext = new CandidateContext(candidate);
        if (testScore >= 70) {
            stateContext.interview();
        } else {
            stateContext.reject();
        }
        candidateRepository.save(candidate);

        return result;
    }
}
