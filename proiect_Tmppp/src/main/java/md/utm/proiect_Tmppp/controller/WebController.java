package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import md.utm.proiect_Tmppp.command.ApproveCandidateCommand;
import md.utm.proiect_Tmppp.command.CandidateReceiver;
import md.utm.proiect_Tmppp.command.RecruiterInvoker;
import md.utm.proiect_Tmppp.command.RejectCandidateCommand;
import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.factory.method.CandidateFactory;
import md.utm.proiect_Tmppp.proxy.CandidateCvProxy;
import md.utm.proiect_Tmppp.proxy.RealCandidateCvService;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import md.utm.proiect_Tmppp.service.CandidateAnalysisService;
import md.utm.proiect_Tmppp.service.JobService;
import md.utm.proiect_Tmppp.service.NotificationService;
import md.utm.proiect_Tmppp.service.RecruitmentProcessService;
import md.utm.proiect_Tmppp.service.SkillService;
import md.utm.proiect_Tmppp.service.TestService;
import md.utm.proiect_Tmppp.singleton.PlatformSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateAnalysisService candidateAnalysisService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private RecruitmentProcessService recruitmentProcessService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TestService testService;

    @Autowired
    private CandidateReceiver candidateReceiver;

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
        if (isPlatformUser(session)) {
            return "redirect:/user-dashboard";
        }
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {
        String normalizedUsername = username == null ? "" : username.trim().toLowerCase();
        Optional<AppUser> optionalUser = appUserRepository.findByUsernameIgnoreCase(normalizedUsername);
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
        try {
            prepareDashboardModel(session, model, true);
        } catch (RuntimeException ex) {
            prepareDashboardFallbackModel(session, model);
        }
        return "user-dashboard";
    }

    @GetMapping("/user-dasboard")
    public String userDasboardAfterRegister(HttpSession session, Model model) {
        try {
            prepareDashboardModel(session, model, true);
        } catch (RuntimeException ex) {
            prepareDashboardFallbackModel(session, model);
        }
        return "user-dashboard";
    }

    private void prepareDashboardFallbackModel(HttpSession session, Model model) {
        String username = String.valueOf(session.getAttribute("currentUsername") != null
                ? session.getAttribute("currentUsername")
                : "user");
        String currentUser = String.valueOf(session.getAttribute("currentUser") != null
                ? session.getAttribute("currentUser")
                : "Utilizator");
        String role = String.valueOf(session.getAttribute("currentRole") != null
                ? session.getAttribute("currentRole")
                : "CANDIDATE");

        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", role);
        }
        if (session.getAttribute("currentUsername") == null) {
            session.setAttribute("currentUsername", username);
        }
        if (session.getAttribute("currentUser") == null) {
            session.setAttribute("currentUser", currentUser);
        }

        AppUser profile = new AppUser(username, "", currentUser, role);
        model.addAttribute("profile", profile);
        model.addAttribute("applications", new ArrayList<Candidate>());
        model.addAttribute("favorites", new ArrayList<JobListing>());
        model.addAttribute("recommendations", new ArrayList<JobListing>());
        model.addAttribute("notifications", splitLines(profile.getNotifications()));
        model.addAttribute("messages", splitLines(profile.getMessages()));
        model.addAttribute("openApplications", 0);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("skillGroups", new ArrayList<>());
        model.addAttribute("skills", new ArrayList<>());
        model.addAttribute("hasSkillGroups", false);
        model.addAttribute("skillCategories", List.of(
                "Backend Skills",
                "Frontend Skills",
                "Database Skills",
                "Soft Skills"
        ));
        model.addAttribute("currentRole", role);
        model.addAttribute("isCandidateDashboard", normalizeRole(role).equals("CANDIDATE") || normalizeRole(role).equals("USER"));
        model.addAttribute("isRecruiterDashboard", normalizeRole(role).equals("RECRUITER"));
        model.addAttribute("recruiterCandidates", new ArrayList<Candidate>());
        model.addAttribute("recruiterJobs", new ArrayList<JobListing>());
        model.addAttribute("tests", new ArrayList<>());
        model.addAttribute("pendingCandidates", 0);
        model.addAttribute("approvedJobs", 0);
        model.addAttribute("pendingJobs", 0);
        model.addAttribute("profileMessage", "Dashboard incarcat in modul sigur. Autentificarea este activa.");
    }

    private void prepareDashboardModel(HttpSession session, Model model, boolean allowGuestFallback) {
        if (session.getAttribute("currentRole") == null) {
            if (!allowGuestFallback) {
                return;
            }
            session.setAttribute("currentRole", "CANDIDATE");
            session.setAttribute("currentUser", "Utilizator");
            session.setAttribute("currentUsername", "user");
        }

        String username = String.valueOf(session.getAttribute("currentUsername") != null
                ? session.getAttribute("currentUsername")
                : "user");
        String currentUser = String.valueOf(session.getAttribute("currentUser") != null
                ? session.getAttribute("currentUser")
                : "Utilizator");

        String role = String.valueOf(session.getAttribute("currentRole"));
        AppUser profile = currentProfile(session).orElseGet(() -> new AppUser(username, "", currentUser, role));
        List<Candidate> applications = loadApplicationsSafely(profile.getUsername());
        Set<Long> favoriteIds = parseFavoriteIdsSafely(profile.getFavoriteJobIds());
        List<JobListing> favorites = loadFavoritesSafely(favoriteIds);
        List<JobListing> recommendations = loadRecommendationsSafely(profile);
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
        model.addAttribute("currentUser", currentUser);
        addSkillModel(model, username);
        enrichDashboardRoleModel(model, session);
        List<Candidate> recruiterCandidates = loadAllCandidatesSafely();
        List<JobListing> recruiterJobs = loadAllJobsSafely();
        model.addAttribute("recruiterCandidates", recruiterCandidates);
        model.addAttribute("recruiterJobs", recruiterJobs);
        model.addAttribute("tests", testService.getAllTests());
        model.addAttribute("pendingCandidates", recruiterCandidates.stream()
                .filter(candidate -> !"Accepted".equalsIgnoreCase(candidate.getStatus()))
                .filter(candidate -> !"Rejected".equalsIgnoreCase(candidate.getStatus()))
                .count());
        model.addAttribute("approvedJobs", recruiterJobs.stream().filter(JobListing::isApproved).count());
        model.addAttribute("pendingJobs", recruiterJobs.stream().filter(job -> !job.isApproved()).count());
        model.addAttribute("registerMessage", session.getAttribute("registerMessage"));
        model.addAttribute("createdPlatformUserName", session.getAttribute("createdPlatformUserName"));
        model.addAttribute("createdPlatformUserEmail", session.getAttribute("createdPlatformUserEmail"));
        session.removeAttribute("registerMessage");
        session.removeAttribute("createdPlatformUserName");
        session.removeAttribute("createdPlatformUserEmail");
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
    public String approveCandidate(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        String commandResult = candidateRepository.findById(id).map(candidate -> {
            RecruiterInvoker invoker = new RecruiterInvoker();
            invoker.setCommand(new ApproveCandidateCommand(candidateReceiver, candidate));
            return invoker.runCommand();
        }).orElse(null);
        if (commandResult == null) {
            redirectAttributes.addFlashAttribute("commandError", "Candidatul selectat nu a fost gasit.");
        } else {
            redirectAttributes.addFlashAttribute("commandMessage", commandResult);
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-candidates" : "redirect:/admin-dashboard#applications";
    }

    @PostMapping("/candidates/reject/{id}")
    public String rejectCandidate(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        String commandResult = candidateRepository.findById(id).map(candidate -> {
            RecruiterInvoker invoker = new RecruiterInvoker();
            invoker.setCommand(new RejectCandidateCommand(candidateReceiver, candidate));
            return invoker.runCommand();
        }).orElse(null);
        if (commandResult == null) {
            redirectAttributes.addFlashAttribute("commandError", "Candidatul selectat nu a fost gasit.");
        } else {
            redirectAttributes.addFlashAttribute("commandMessage", commandResult);
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-candidates" : "redirect:/admin-dashboard#applications";
    }

    @PostMapping("/admin/candidates/delete/{id}")
    public String deleteCandidate(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/login";
        }
        if (candidateRepository.existsById(id)) {
            candidateRepository.deleteById(id);
        }
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
        model.addAttribute("isCandidateDashboard", isCandidate(session));
        model.addAttribute("isRecruiterDashboard", isRecruiter(session));
        model.addAttribute("isAdminDashboard", isAdmin(session));
        return "jobs";
    }

    @PostMapping("/jobs/apply/{id}")
    public String applyJob(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isCandidate(session)) {
            redirectAttributes.addFlashAttribute("jobMessage", "Trebuie sa te autentifici ca candidat pentru a aplica.");
            return "redirect:/login";
        }

        Optional<JobListing> optionalJob = jobListingRepository.findById(id);
        if (optionalJob.isPresent()) {
            JobListing job = optionalJob.get();
            AppUser profile = currentProfile(session).orElse(null);
            String candidateEmail = String.valueOf(session.getAttribute("currentUsername"));
            Candidate application = candidateRepository.findByEmail(candidateEmail).stream()
                    .filter(candidate -> isBlank(candidate.getAppliedJobTitle())
                            || job.getTitle().equalsIgnoreCase(candidate.getAppliedJobTitle()))
                    .findFirst()
                    .orElseGet(() -> candidateRepository.findByEmail(candidateEmail).stream()
                            .findFirst()
                            .orElseGet(() -> new Candidate(
                                    profile != null ? profile.getFullName() : String.valueOf(session.getAttribute("currentUser")),
                                    candidateEmail)));

            application.setName(profile != null ? profile.getFullName() : String.valueOf(session.getAttribute("currentUser")));
            application.setEmail(candidateEmail);
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

            notificationService.notifyRecruitersAboutApplication(application, job);
            redirectAttributes.addFlashAttribute("jobMessage",
                    "Cererea a fost trimisa catre recrutor pentru jobul '" + job.getTitle()
                            + "'. Asteptati raspunsul recruiterului.");
        } else {
            redirectAttributes.addFlashAttribute("jobMessage", "Jobul nu a fost gasit.");
        }
        return "redirect:/jobs";
    }

    @PostMapping("/jobs/favorite/{id}")
    public String toggleFavorite(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isCandidate(session)) {
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
        if (!isPlatformUser(session)) {
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

    @PostMapping("/skills/add")
    public String addSkill(@RequestParam String category,
                           @RequestParam String name,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (!isCandidate(session)) {
            return "redirect:/login";
        }
        String username = String.valueOf(session.getAttribute("currentUsername"));
        skillService.addSkill(username, category, name);
        redirectAttributes.addFlashAttribute("profileMessage", "Skill-ul a fost adaugat in profil.");
        return "redirect:/user-dashboard#my-skills";
    }

    @PostMapping("/skills/delete/{id}")
    public String deleteSkill(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (!isCandidate(session)) {
            return "redirect:/login";
        }
        String username = String.valueOf(session.getAttribute("currentUsername"));
        skillService.deleteSkill(id, username);
        redirectAttributes.addFlashAttribute("profileMessage", "Skill-ul a fost sters.");
        return "redirect:/user-dashboard#my-skills";
    }

    @PostMapping("/messages/send")
    public String sendMessage(@RequestParam String message, HttpSession session) {
        if (!isPlatformUser(session)) {
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
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        boolean isNewJob = id == null;
        JobListing job = isNewJob ? new JobListing() : jobListingRepository.findById(id).orElse(new JobListing());
        job.setTitle(title);
        job.setDescription(description);
        job.setDomain(domain);
        job.setLocation(location);
        job.setExperienceLevel(experienceLevel);
        job.setSalary(salary);
        job.setApproved(approved);
        jobListingRepository.save(job);
        if (isNewJob && approved) {
            notificationService.notifyCandidatesAboutNewJob(job);
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-jobs" : "redirect:/admin-dashboard#jobs-admin";
    }

    @PostMapping("/admin/jobs/approval/{id}")
    public String updateJobApproval(@PathVariable Long id, @RequestParam boolean approved, HttpSession session) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        jobListingRepository.findById(id).ifPresent(job -> {
            job.setApproved(approved);
            jobListingRepository.save(job);
        });
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-jobs" : "redirect:/admin-dashboard#jobs-admin";
    }

    @GetMapping("/admin/jobs/clone/{id}")
    @ResponseBody
    public ResponseEntity<JobListing> cloneJob(@PathVariable Long id, HttpSession session) {
        if (!isRecruiterOrAdmin(session)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return jobService.cloneJob(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/admin/jobs/delete/{id}")
    public String deleteJob(@PathVariable Long id, HttpSession session) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        if (jobListingRepository.existsById(id)) {
            jobListingRepository.deleteById(id);
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-jobs" : "redirect:/admin-dashboard#jobs-admin";
    }

    @PostMapping("/admin/users/toggle/{id}")
    public String toggleUser(@PathVariable Long id, HttpSession session) {
        if (!isRecruiterOrAdmin(session)) {
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

    @GetMapping("/my-skills")
    public String mySkills(HttpSession session) {
        if (!isCandidate(session)) {
            return "redirect:/login";
        }
        return "redirect:/user-dashboard#my-skills";
    }

    @PostMapping("/candidates/evaluate/{id}")
    public String evaluateCandidate(@PathVariable Long id,
                                    @RequestParam(defaultValue = "85") int testScore,
                                    @RequestParam(required = false) String evaluationMethod,
                                    @RequestParam(defaultValue = "0") int experienceYears,
                                    @RequestParam(required = false) String recruiterNotes,
                                    HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        try {
            String message = evaluationMethod == null || "testScore".equalsIgnoreCase(evaluationMethod)
                    ? candidateAnalysisService.analyzeCandidate(id, testScore, recruiterNotes)
                    : candidateAnalysisService.analyzeCandidate(id, testScore, experienceYears, evaluationMethod, recruiterNotes);
            redirectAttributes.addFlashAttribute("adapterEvaluationMessage", message);
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("adapterEvaluationMessage", "Candidate could not be evaluated.");
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-candidates" : "redirect:/admin-dashboard#applications";
    }

    @PostMapping("/recruitment/process/{id}")
    public String processRecruitment(@PathVariable Long id,
                                     @RequestParam(defaultValue = "85") int testScore,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        if (!isRecruiterOrAdmin(session)) {
            return "redirect:/login";
        }
        try {
            List<String> result = recruitmentProcessService.processCandidate(id, testScore);
            redirectAttributes.addFlashAttribute("recruitmentProcessMessage", String.join(" | ", result));
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("recruitmentProcessMessage", "Recruitment process could not be completed.");
        }
        return isRecruiter(session) ? "redirect:/user-dashboard#recruiter-candidates" : "redirect:/admin-dashboard#applications";
    }

    @GetMapping("/candidates/cv/{id}")
    public String viewCandidateCv(@PathVariable Long id, HttpSession session, Model model) {
        if (!isRecruiterOrAdmin(session)) {
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
        return hasRole(session, "ADMIN");
    }

    private boolean isUser(HttpSession session) {
        return hasRole(session, "USER");
    }

    private boolean isCandidate(HttpSession session) {
        return hasRole(session, "CANDIDATE") || hasRole(session, "USER");
    }

    private boolean isRecruiter(HttpSession session) {
        return hasRole(session, "RECRUITER");
    }

    private boolean isPlatformUser(HttpSession session) {
        return isCandidate(session) || isRecruiter(session) || isUser(session);
    }

    private boolean isRecruiterOrAdmin(HttpSession session) {
        return isAdmin(session) || isRecruiter(session);
    }

    private boolean hasRole(HttpSession session, String expectedRole) {
        String expected = normalizeRole(expectedRole);
        String sessionRole = normalizeRole(String.valueOf(session.getAttribute("currentRole")));
        if (expected.equals(sessionRole)) {
            return true;
        }

        Object username = session.getAttribute("currentUsername");
        if (username == null) {
            return false;
        }

        return appUserRepository.findByUsername(String.valueOf(username))
                .map(AppUser::getRole)
                .map(this::normalizeRole)
                .filter(expected::equals)
                .isPresent();
    }

    private String normalizeRole(String role) {
        if (role == null || "null".equalsIgnoreCase(role)) {
            return "";
        }
        String normalized = role.trim().toUpperCase();
        if ("RECRUITER".equals(normalized) || "RECRUTOR".equals(normalized) || "RECRUITER_ROLE".equals(normalized)) {
            return "RECRUITER";
        }
        if ("CANDIDATE".equals(normalized) || "CANDIDAT".equals(normalized) || "USER".equals(normalized)) {
            return normalized;
        }
        return normalized;
    }

    private void enrichDashboardRoleModel(Model model, HttpSession session) {
        String role = String.valueOf(session.getAttribute("currentRole"));
        model.addAttribute("currentRole", role);
        model.addAttribute("isCandidateDashboard", isCandidate(session));
        model.addAttribute("isRecruiterDashboard", isRecruiter(session));
    }

    private void addSkillModel(Model model, String username) {
        List<?> skillGroups = skillService.getSkillGroups(username);
        List<?> skills = skillService.getSkills(username);
        model.addAttribute("skillGroups", skillGroups);
        model.addAttribute("skills", skills);
        model.addAttribute("hasSkillGroups", !skillGroups.isEmpty());
        model.addAttribute("skillCategories", List.of(
                "Backend Skills",
                "Frontend Skills",
                "Database Skills",
                "Soft Skills"
        ));
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

    private Set<Long> parseFavoriteIdsSafely(String value) {
        try {
            return parseFavoriteIds(value);
        } catch (NumberFormatException ex) {
            return new LinkedHashSet<>();
        }
    }

    private List<Candidate> loadApplicationsSafely(String username) {
        try {
            return candidateRepository.findByEmail(username);
        } catch (RuntimeException ex) {
            return new ArrayList<>();
        }
    }

    private List<JobListing> loadFavoritesSafely(Set<Long> favoriteIds) {
        try {
            return favoriteIds.isEmpty() ? new ArrayList<>() : jobListingRepository.findAllById(favoriteIds);
        } catch (RuntimeException ex) {
            return new ArrayList<>();
        }
    }

    private List<JobListing> loadRecommendationsSafely(AppUser profile) {
        try {
            return jobListingRepository.findAll().stream()
                    .filter(JobListing::isApproved)
                    .filter(job -> containsIgnoreCase(job.getDomain(), profile.getDomainPreference())
                            || containsIgnoreCase(job.getLocation(), profile.getLocationPreference()))
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (RuntimeException ex) {
            return new ArrayList<>();
        }
    }

    private List<Candidate> loadAllCandidatesSafely() {
        try {
            return candidateRepository.findAll();
        } catch (RuntimeException ex) {
            return new ArrayList<>();
        }
    }

    private List<JobListing> loadAllJobsSafely() {
        try {
            return jobListingRepository.findAll();
        } catch (RuntimeException ex) {
            return new ArrayList<>();
        }
    }

    private String joinIds(Set<Long> ids) {
        return ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private void appendNotification(AppUser user, String notification) {
        String existing = user.getNotifications() == null ? "" : user.getNotifications();
        user.setNotifications(existing + "\n" + notification);
    }
}
