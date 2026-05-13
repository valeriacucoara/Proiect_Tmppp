package md.utm.proiect_Tmppp.command;

import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.service.NotificationService;
import md.utm.proiect_Tmppp.state.CandidateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CandidateReceiver {

    private CandidateRepository candidateRepository;
    private NotificationService notificationService;

    public CandidateReceiver() {
    }

    @Autowired
    public CandidateReceiver(CandidateRepository candidateRepository, NotificationService notificationService) {
        this.candidateRepository = candidateRepository;
        this.notificationService = notificationService;
    }

    // Receiver: knows how to execute the real business operation requested by a command.
    public String approve(Candidate candidate) {
        CandidateContext context = new CandidateContext(candidate);
        context.accept();
        candidate.setRecruiterMessage("Candidatura a fost acceptata. Candidatul a primit notificare.");
        candidateRepository.save(candidate);
        notificationService.notifyCandidateAboutDecision(candidate, true);
        return "Candidatul " + candidate.getName() + " a fost acceptat.";
    }

    // Receiver: handles the rejection operation and persists the candidate status.
    public String reject(Candidate candidate) {
        CandidateContext context = new CandidateContext(candidate);
        context.reject();
        candidate.setRecruiterMessage("Candidatura a fost respinsa. Candidatul a primit notificare.");
        candidateRepository.save(candidate);
        notificationService.notifyCandidateAboutDecision(candidate, false);
        return "Candidatul " + candidate.getName() + " a fost respins.";
    }

    public String approve(String candidateName) {
        return "Candidatul " + candidateName + " a fost aprobat.";
    }

    public String reject(String candidateName) {
        return "Candidatul " + candidateName + " a fost respins.";
    }

    public String scheduleInterview(String candidateName) {
        return "Interviul pentru " + candidateName + " a fost programat.";
    }
}
