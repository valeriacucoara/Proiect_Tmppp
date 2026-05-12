package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.factory.method.CandidateFactory;
import md.utm.proiect_Tmppp.observer.CandidateObserver;
import md.utm.proiect_Tmppp.observer.JobApplicationSubject;
import md.utm.proiect_Tmppp.observer.RecruiterObserver;
import md.utm.proiect_Tmppp.proxy.CandidateCvProxy;
import md.utm.proiect_Tmppp.proxy.RealCandidateCvService;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import md.utm.proiect_Tmppp.singleton.PlatformSettings;
import md.utm.proiect_Tmppp.state.AcceptedState;
import md.utm.proiect_Tmppp.state.RejectedState;
import md.utm.proiect_Tmppp.strategy.CandidateEvaluator;
import md.utm.proiect_Tmppp.strategy.CompositeEvaluationStrategy;
import md.utm.proiect_Tmppp.strategy.ExperienceStrategy;
import md.utm.proiect_Tmppp.strategy.ScoreStrategy;
import md.utm.proiect_Tmppp.strategy.SkillStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobListingRepository jobListingRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        PlatformSettings settings = PlatformSettings.getInstance();
        model.addAttribute("platformName", settings.getPlatformName());
        model.addAttribute("platformVersion", settings.getVersion());
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "home";
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (isAdmin(session)) {
            return "redirect:/admin-dashboard";
        }
        if (isUser(session)) {
            return "redirect:/user-dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        Optional<AppUser> optionalUser = appUserRepository.findByUsername(username);
        if (optionalUser.isEmpty() || !password.equals(optionalUser.get().getPassword())) {
            model.addAttribute("error", "Email sau parola incorecta.");
            return "login";
        }

        AppUser user = optionalUser.get();
        if (!user.isActive()) {
            model.addAttribute("error", "Contul este suspendat.");
            return "login";
        }

        session.setAttribute("currentUser", user.getFullName());
        session.setAttribute("currentUsername", user.getUsername());
        session.setAttribute("currentRole", user.getRole());
        return "ADMIN".equalsIgnoreCase(user.getRole()) ? "redirect:/admin-dashboard" : "redirect:/user-dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/user-dashboard")
    public String userDashboard(HttpSession session, Model model) {
        if (!isUser(session)) {
            return "redirect:/login";
        }

        AppUser profile = currentProfile(session).orElseGet(() ->
                new AppUser(String.valueOf(session.getAttribute("currentUsername")), "", String.valueOf(session.getAttribute("currentUser")), "USER"));
        List<Candidate> applications = candidateRepository.findByEmail(profile.getUsername());
        Set<Long> favoriteIds = parseFavoriteIds(profile.getFavoriteJobIds());
        List<JobListing> favorites = favoriteIds.isEmpty() ? new ArrayList<>() : jobListingRepository.findAllById(favoriteIds);
        List<JobListing> recommendations = jobListingRepository.findAll().stream()
                .filter(JobListing::isApproved)
                .filter(job -> containsIgnoreCase(job.getDomain(), profile.getDomainPreference())
                        || containsIgnoreCase(job.getLocation(), profile.getLocationPreference()))
                .limit(5)
                .collect(Collectors.toList());
        long openApplications = applications.stream()
                .filter(candidate -> !"Accepted".equalsIgnoreCase(candidate.getStatus()))
                .filter(candidate -> !"Rejected".equalsIgnoreCase(candidate.getStatus()))
                .count();

        model.addAttribute("profile", profile);
        model.addAttribute("applications", applications);
        model.addAttribute("favorites", favorites);
        model.addAttribute("recommendations", recommendations);
        model.addAttribute("notifications", splitLines(profile.getNotifications()));
        model.addAttribute("messages", splitLines(profile.getMessages()));
        model.addAttribute("openApplications", openApplications);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "user-dashboard";
    }

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }

        List<JobListing> jobs = jobListingRepository.findAll();
        List<Candidate> candidates = candidateRepository.findAll();
        List<AppUser> users = appUserRepository.findAll();
        model.addAttribute("jobs", jobs);
        model.addAttribute("candidates", candidates);
        model.addAttribute("users", users);
        model.addAttribute("approvedJobs", jobs.stream().filter(JobListing::isApproved).count());
        model.addAttribute("pendingJobs", jobs.stream().filter(job -> !job.isApproved()).count());
        model.addAttribute("activeUsers", users.stream().filter(AppUser::isActive).count());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "admin-dashboard";
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("candidates", candidateRepository.findAll());
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "candidates";
    }

    @GetMapping("/add-candidate")
    public String addCandidateForm(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("candidate", new Candidate());
        return "add-candidate";
    }

    @PostMapping("/add-candidate")
    public String addCandidateSubmit(@ModelAttribute Candidate candidate, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        CandidateFactory factory = new CandidateFactory();
        Candidate newCandidate = (Candidate) factory.createUser();
        newCandidate.setName(candidate.getName());
        newCandidate.setEmail(candidate.getEmail());
        newCandidate.setSkill(candidate.getSkill() != null ? candidate.getSkill() : "N/A");
        newCandidate.setStatus("Applied");
        newCandidate.setAppliedJobTitle(candidate.getAppliedJobTitle());
        newCandidate.setExpectedSalary(candidate.getExpectedSalary());
        newCandidate.setCv(candidate.getCv());
        candidateRepository.save(newCandidate);
        return "redirect:/candidates";
    }

    @PostMapping("/candidates/approve/{id}")
    public String approveCandidate(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        candidateRepository.findById(id).ifPresent(candidate -> {
            candidate.setState(new AcceptedState());
            candidateRepository.save(candidate);
        });
        return "redirect:/admin-dashboard#applications";
    }

    @PostMapping("/candidates/reject/{id}")
    public String rejectCandidate(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        candidateRepository.findById(id).ifPresent(candidate -> {
            candidate.setState(new RejectedState());
            candidateRepository.save(candidate);
        });
        return "redirect:/admin-dashboard#applications";
    }

    @GetMapping("/jobs")
    public String jobs(@RequestParam(required = false) String query,
                       @RequestParam(required = false) String domain,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) String experience,
                       @RequestParam(required = false) Double minSalary,
                       Model model,
                       HttpSession session) {
        List<JobListing> allJobs = jobListingRepository.findAll().stream()
                .filter(job -> isAdmin(session) || job.isApproved())
                .collect(Collectors.toList());
        List<JobListing> filteredJobs = allJobs.stream()
                .filter(job -> isBlank(query)
                        || containsIgnoreCase(job.getTitle(), query)
                        || containsIgnoreCase(job.getDescription(), query)
                        || containsIgnoreCase(job.getDomain(), query))
                .filter(job -> isBlank(domain) || domain.equals(job.getDomain()))
                .filter(job -> isBlank(location) || location.equals(job.getLocation()))
                .filter(job -> isBlank(experience) || experience.equals(job.getExperienceLevel()))
                .filter(job -> minSalary == null || job.getSalary() >= minSalary)
                .collect(Collectors.toList());

        Set<Long> favoriteIds = currentProfile(session)
                .map(profile -> parseFavoriteIds(profile.getFavoriteJobIds()))
                .orElse(Collections.emptySet());
        model.addAttribute("jobs", filteredJobs);
        model.addAttribute("domains", distinctValues(allJobs, "domain"));
        model.addAttribute("locations", distinctValues(allJobs, "location"));
        model.addAttribute("experiences", distinctValues(allJobs, "experience"));
        model.addAttribute("favoriteIds", favoriteIds);
        model.addAttribute("query", query);
        model.addAttribute("domain", domain);
        model.addAttribute("location", location);
        model.addAttribute("experience", experience);
        model.addAttribute("minSalary", minSalary);
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "jobs";
    }

    @PostMapping("/jobs/apply/{id}")
    public String applyJob(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isUser(session)) {
            redirectAttributes.addFlashAttribute("jobMessage", "Trebuie sa te autentifici ca utilizator pentru a aplica.");
            return "redirect:/login";
        }

        Optional<JobListing> optionalJob = jobListingRepository.findById(id);
        if (optionalJob.isPresent()) {
            JobListing job = optionalJob.get();
            AppUser profile = currentProfile(session).orElse(null);
            Candidate application = new Candidate(
                    profile != null ? profile.getFullName() : String.valueOf(session.getAttribute("currentUser")),
                    String.valueOf(session.getAttribute("currentUsername")));
            application.setSkill(profile != null ? profile.getDomainPreference() : "N/A");
            application.setAppliedJobTitle(job.getTitle());
            application.setExpectedSalary(job.getSalary());
            application.setCv(profile != null ? profile.getCv() : "CV not provided yet.");
            application.setStatus("Applied");
            application.setRecruiterMessage("Aplicatia a fost trimisa si asteapta verificarea recruiterului.");
            candidateRepository.save(application);

            if (profile != null) {
                appendNotification(profile, "Ai aplicat la jobul: " + job.getTitle());
                appUserRepository.save(profile);
            }

            JobApplicationSubject subject = new JobApplicationSubject();
            subject.attach(new RecruiterObserver("HR Manager"));
            subject.attach(new CandidateObserver("System"));
            subject.setApplicationDetails(String.valueOf(session.getAttribute("currentUser")), job.getTitle());
            subject.updateStatus("Applied");
            redirectAttributes.addFlashAttribute("jobMessage", "Ai aplicat cu succes pentru jobul '" + job.getTitle() + "'.");
        } else {
            redirectAttributes.addFlashAttribute("jobMessage", "Jobul nu a fost gasit.");
        }
        return "redirect:/jobs";
    }

    @PostMapping("/jobs/favorite/{id}")
    public String toggleFavorite(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isUser(session)) {
            redirectAttributes.addFlashAttribute("jobMessage", "Trebuie sa te autentifici pentru a salva joburi favorite.");
            return "redirect:/login";
        }
        currentProfile(session).ifPresent(user -> {
            Set<Long> ids = parseFavoriteIds(user.getFavoriteJobIds());
            if (ids.contains(id)) {
                ids.remove(id);
            } else {
                ids.add(id);
            }
            user.setFavoriteJobIds(joinIds(ids));
            appUserRepository.save(user);
        });
        return "redirect:/jobs";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String fullName,
                                @RequestParam String domainPreference,
                                @RequestParam String locationPreference,
                                @RequestParam int experienceYears,
                                @RequestParam(required = false) String cv,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (!isUser(session)) {
            return "redirect:/login";
        }
        currentProfile(session).ifPresent(user -> {
            user.setFullName(fullName);
            user.setDomainPreference(domainPreference);
            user.setLocationPreference(locationPreference);
            user.setExperienceYears(experienceYears);
            user.setCv(cv == null ? "" : cv);
            appUserRepository.save(user);
            session.setAttribute("currentUser", fullName);
        });
        redirectAttributes.addFlashAttribute("profileMessage", "Profilul a fost actualizat.");
        return "redirect:/user-dashboard#profile";
    }

    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam String message, HttpSession session) {
        if (!isUser(session)) {
            return "redirect:/login";
        }
        currentProfile(session).ifPresent(user -> {
            String existing = user.getMessages() == null ? "" : user.getMessages();
            user.setMessages(existing + "\nTu: " + message + "\nRecruiter: Multumim, revenim cu raspuns in curand.");
            appUserRepository.save(user);
        });
        return "redirect:/user-dashboard#messages";
    }

    @PostMapping("/admin/jobs/save")
    public String saveJob(@RequestParam(required = false) Long id,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String domain,
                          @RequestParam String location,
                          @RequestParam String experienceLevel,
                          @RequestParam double salary,
                          @RequestParam(defaultValue = "false") boolean approved,
                          HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        JobListing job = id == null ? new JobListing() : jobListingRepository.findById(id).orElse(new JobListing());
        job.setTitle(title);
        job.setDescription(description);
        job.setDomain(domain);
        job.setLocation(location);
        job.setExperienceLevel(experienceLevel);
        job.setSalary(salary);
        job.setApproved(approved);
        jobListingRepository.save(job);
        return "redirect:/admin-dashboard#jobs-admin";
    }

    @PostMapping("/admin/jobs/approval/{id}")
    public String updateJobApproval(@PathVariable Long id, @RequestParam boolean approved, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        jobListingRepository.findById(id).ifPresent(job -> {
            job.setApproved(approved);
            jobListingRepository.save(job);
        });
        return "redirect:/admin-dashboard#jobs-admin";
    }

    @PostMapping("/admin/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        jobListingRepository.deleteById(id);
        return "redirect:/admin-dashboard#jobs-admin";
    }

    @PostMapping("/admin/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        appUserRepository.findById(id).ifPresent(user -> {
            user.setActive(!user.isActive());
            appUserRepository.save(user);
        });
        return "redirect:/admin-dashboard#users";
    }

    @PostMapping("/admin/notify")
    public String notifyUsers(@RequestParam String notification, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        appUserRepository.findAll().stream()
                .filter(user -> "USER".equalsIgnoreCase(user.getRole()))
                .forEach(user -> {
                    appendNotification(user, "Admin: " + notification);
                    appUserRepository.save(user);
                });
        return "redirect:/admin-dashboard";
    }

    @GetMapping("/users")
    public String users(Model model, HttpSession session) {
        if (!isUser(session) && !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("userCount", appUserRepository.count());
        model.addAttribute("studentsActive", appUserRepository.findAll().stream().filter(AppUser::isActive).count());
        model.addAttribute("teachersCount", candidateRepository.count());
        return "users";
    }

    @GetMapping("/courses")
    public String courses(HttpSession session, Model model) {
        if (!isUser(session) && !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "courses";
    }

    @GetMapping("/lessons")
    public String lessons(HttpSession session, Model model) {
        if (!isUser(session) && !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "lessons";
    }

    @GetMapping("/evaluations")
    public String evaluations(HttpSession session, Model model) {
        if (!isUser(session) && !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "evaluations";
    }

    @GetMapping("/statistics")
    public String statistics(HttpSession session, Model model) {
        if (!isUser(session) && !isAdmin(session)) {
            return "redirect:/login";
        }
        model.addAttribute("currentRole", session.getAttribute("currentRole"));
        return "statistics";
    }

    @PostMapping("/candidates/evaluate/{id}")
    public String evaluateCandidate(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        candidateRepository.findById(id).ifPresent(candidate -> {
            CandidateEvaluator evaluator = new CandidateEvaluator();
            CompositeEvaluationStrategy compositeStrategy = new CompositeEvaluationStrategy();
            compositeStrategy.addStrategy(new ScoreStrategy());
            compositeStrategy.addStrategy(new ExperienceStrategy());
            compositeStrategy.addStrategy(new SkillStrategy());
            evaluator.setStrategy(compositeStrategy);
            int skillCount = candidate.getSkill() == null ? 0 : candidate.getSkill().split(",").length;
            String evaluation = evaluator.evaluateCandidate(candidate.getName(), 85, 3, skillCount);
            candidate.setRecruiterMessage(evaluation);
            candidateRepository.save(candidate);
        });
        return "redirect:/admin-dashboard#applications";
    }

    @GetMapping("/candidates/cv/{id}")
    public String viewCandidateCv(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        candidateRepository.findById(id).ifPresent(candidate -> {
            RealCandidateCvService realService = new RealCandidateCvService(candidate.getCv());
            CandidateCvProxy proxy = new CandidateCvProxy(realService, "recruiter");
            model.addAttribute("cvContent", proxy.viewCv());
            model.addAttribute("candidateName", candidate.getName());
        });
        return "candidate-cv";
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equalsIgnoreCase(String.valueOf(session.getAttribute("currentRole")));
    }

    private boolean isUser(HttpSession session) {
        return "USER".equalsIgnoreCase(String.valueOf(session.getAttribute("currentRole")));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private boolean containsIgnoreCase(String value, String query) {
        return value != null && query != null && value.toLowerCase().contains(query.toLowerCase());
    }

    private Set<String> distinctValues(List<JobListing> jobs, String field) {
        return jobs.stream()
                .map(job -> switch (field) {
                    case "domain" -> job.getDomain();
                    case "location" -> job.getLocation();
                    case "experience" -> job.getExperienceLevel();
                    default -> "";
                })
                .filter(value -> value != null && !value.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Optional<AppUser> currentProfile(HttpSession session) {
        Object username = session.getAttribute("currentUsername");
        return username == null ? Optional.empty() : appUserRepository.findByUsername(String.valueOf(username));
    }

    private List<String> splitLines(String value) {
        if (value == null || value.isBlank()) {
            return new ArrayList<>();
        }
        return Arrays.stream(value.split("\\R"))
                .filter(line -> !line.isBlank())
                .collect(Collectors.toList());
    }

    private Set<Long> parseFavoriteIds(String value) {
        if (value == null || value.isBlank()) {
            return new LinkedHashSet<>();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .map(Long::valueOf)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String joinIds(Set<Long> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private void appendNotification(AppUser user, String notification) {
        String existing = user.getNotifications() == null ? "" : user.getNotifications();
        user.setNotifications(existing + "\n" + notification);
    }
}
