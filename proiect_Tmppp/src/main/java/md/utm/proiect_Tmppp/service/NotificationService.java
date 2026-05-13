package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.observer.CandidateObserver;
import md.utm.proiect_Tmppp.observer.JobSubject;
import md.utm.proiect_Tmppp.observer.RecruiterObserver;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private AppUserRepository appUserRepository;

    public void notifyRecruitersAboutApplication(Candidate candidate, JobListing job) {
        String message = "New candidate applied for " + job.getTitle();
        List<AppUser> recruiters = appUserRepository.findAll().stream()
                .filter(user -> isRole(user, "RECRUITER") || isRole(user, "ADMIN"))
                .toList();

        for (AppUser recruiter : recruiters) {
            JobSubject subject = new JobSubject();
            subject.attach(new RecruiterObserver(recruiter.getFullName()));
            appendNotification(recruiter, subject.notifyObservers(message).get(0));
            appUserRepository.save(recruiter);
        }
    }

    public void notifyCandidateAboutDecision(Candidate candidate, boolean accepted) {
        String message = accepted ? "Your application was accepted" : "Your application was rejected";
        appUserRepository.findByUsernameIgnoreCase(candidate.getEmail()).ifPresent(user -> {
            JobSubject subject = new JobSubject();
            subject.attach(new CandidateObserver(user.getFullName()));
            appendNotification(user, subject.notifyObservers(message).get(0));
            appUserRepository.save(user);
        });
    }

    public void notifyCandidatesAboutNewJob(JobListing job) {
        String message = "New job available: " + job.getTitle();
        List<AppUser> candidates = appUserRepository.findAll().stream()
                .filter(user -> isRole(user, "CANDIDATE") || isRole(user, "USER"))
                .toList();

        for (AppUser candidate : candidates) {
            JobSubject subject = new JobSubject();
            subject.attach(new CandidateObserver(candidate.getFullName()));
            appendNotification(candidate, subject.notifyObservers(message).get(0));
            appUserRepository.save(candidate);
        }
    }

    private boolean isRole(AppUser user, String role) {
        return user.getRole() != null && user.getRole().equalsIgnoreCase(role);
    }

    private void appendNotification(AppUser user, String notification) {
        String existing = user.getNotifications() == null ? "" : user.getNotifications();
        user.setNotifications(existing.isBlank() ? notification : existing + "\n" + notification);
    }
}
