package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import md.utm.proiect_Tmppp.service.CandidateService;
import md.utm.proiect_Tmppp.service.JobService;
import md.utm.proiect_Tmppp.service.TestService;
import md.utm.proiect_Tmppp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    private JobService jobService;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestService testService;

    @GetMapping("/admin-dashboard")
    public String adminDashboard(Model model, HttpSession session) {
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
            session.setAttribute("currentUser", "ADMIN");
            session.setAttribute("currentUsername", "admin");
        }
        if (!"ADMIN".equalsIgnoreCase(String.valueOf(session.getAttribute("currentRole")))) {
            return "redirect:/login";
        }

        model.addAttribute("jobs", jobService.getAllJobListings());

        model.addAttribute("approvedJobs",
                jobService.getAllJobListings()
                        .stream()
                        .filter(job -> job.isApproved())
                        .count());

        model.addAttribute("pendingJobs",
                jobService.getAllJobListings()
                        .stream()
                        .filter(job -> !job.isApproved())
                        .count());

        model.addAttribute("users", userService.getAllUsers());

        model.addAttribute("activeUsers",
                userService.getAllUsers().stream().filter(user -> user.isActive()).count());

        model.addAttribute("candidates", candidateService.getAllCandidates());
        model.addAttribute("tests", testService.getAllTests());

        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("currentRole", session.getAttribute("currentRole"));

        return "admin-dashboard";
    }
}
