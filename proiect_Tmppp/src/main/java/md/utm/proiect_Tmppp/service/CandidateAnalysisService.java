package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.adapter.CandidateEvaluator;
import md.utm.proiect_Tmppp.adapter.ExternalEvaluationService;
import md.utm.proiect_Tmppp.adapter.TestEvaluationAdapter;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.state.CandidateContext;
import md.utm.proiect_Tmppp.strategy.CVStrategy;
import md.utm.proiect_Tmppp.strategy.CandidateEvaluationContext;
import md.utm.proiect_Tmppp.strategy.EvaluationStrategy;
import md.utm.proiect_Tmppp.strategy.ExperienceStrategy;
import md.utm.proiect_Tmppp.strategy.TestScoreStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateAnalysisService {

    @Autowired
    private CandidateRepository candidateRepository;

    public String analyzeCandidate(Long candidateId, int testScore) {
        return analyzeCandidate(candidateId, testScore, "");
    }

    public String analyzeCandidate(Long candidateId, int testScore, String recruiterNotes) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        CandidateEvaluator evaluator = new TestEvaluationAdapter(new ExternalEvaluationService());
        String result = evaluator.evaluateCandidate(candidate, testScore);
        String message = "Candidate evaluated successfully. Result: " + result;
        String notes = recruiterNotes == null ? "" : recruiterNotes.trim();
        String savedMessage = notes.isEmpty() ? message : message + ". Notes: " + notes;

        candidate.setRecruiterMessage(savedMessage);
        applyEvaluationState(candidate, "PASS".equals(result));
        candidateRepository.save(candidate);

        return savedMessage;
    }

    public String analyzeCandidate(Long candidateId,
                                   int testScore,
                                   int experienceYears,
                                   String evaluationMethod,
                                   String recruiterNotes) {
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));

        CandidateEvaluationContext context = new CandidateEvaluationContext();
        context.setStrategy(resolveStrategy(evaluationMethod));

        String result = context.evaluateCandidate(candidate, testScore, experienceYears, recruiterNotes);
        String notes = recruiterNotes == null ? "" : recruiterNotes.trim();
        String savedMessage = notes.isEmpty() ? result : result + ". Notes: " + notes;

        candidate.setRecruiterMessage(savedMessage);
        applyEvaluationState(candidate, savedMessage.contains("Result: PASS"));
        candidateRepository.save(candidate);

        return savedMessage;
    }

    private EvaluationStrategy resolveStrategy(String evaluationMethod) {
        if ("cv".equalsIgnoreCase(evaluationMethod)) {
            return new CVStrategy();
        }
        if ("experience".equalsIgnoreCase(evaluationMethod)) {
            return new ExperienceStrategy();
        }
        return new TestScoreStrategy();
    }

    private void applyEvaluationState(Candidate candidate, boolean passed) {
        CandidateContext stateContext = new CandidateContext(candidate);
        if (passed) {
            stateContext.interview();
        } else {
            stateContext.reject();
        }
    }
}
