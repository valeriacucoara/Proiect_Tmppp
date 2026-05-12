package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/state")
public class StateController {

    @Autowired
    private CandidateRepository candidateRepository;

    @GetMapping("/status/{id}")
    public String getStatus(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            CandidateContext context = new CandidateContext(candidate);
            return "Status curent: " + context.getCurrentStatus() + " - " + context.request();
        }
        return "Candidate not found";
    }

    @PostMapping("/interview/{id}")
    public String interviewCandidate(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            CandidateContext context = new CandidateContext(candidate);
            context.interview();
            candidateRepository.save(candidate);
            return "Candidate interviewed. New status: " + context.getCurrentStatus();
        }
        return "Candidate not found";
    }

    @PostMapping("/accept/{id}")
    public String acceptCandidate(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            CandidateContext context = new CandidateContext(candidate);
            context.accept();
            candidateRepository.save(candidate);
            return "Candidate accepted. New status: " + context.getCurrentStatus();
        }
        return "Candidate not found";
    }

    @PostMapping("/reject/{id}")
    public String rejectCandidate(@PathVariable Long id) {
        Candidate candidate = candidateRepository.findById(id).orElse(null);
        if (candidate != null) {
            CandidateContext context = new CandidateContext(candidate);
            context.reject();
            candidateRepository.save(candidate);
            return "Candidate rejected. New status: " + context.getCurrentStatus();
        }
        return "Candidate not found";
    }
}