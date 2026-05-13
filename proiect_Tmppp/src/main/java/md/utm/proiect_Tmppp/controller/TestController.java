package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import md.utm.proiect_Tmppp.entity.Test;
import md.utm.proiect_Tmppp.service.TestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @PostMapping("/admin/tests/create")
    public String createTest(@RequestParam String title,
                             @RequestParam String questions,
                             @RequestParam String difficulty,
                             @RequestParam int duration,
                             @RequestParam String domain,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        String role = String.valueOf(session.getAttribute("currentRole"));
        if (!"ADMIN".equalsIgnoreCase(role) && !"RECRUITER".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        Test createdTest = testService.createTest(title, questions, difficulty, duration, domain);
        redirectAttributes.addFlashAttribute("createdTest", createdTest);
        if ("RECRUITER".equalsIgnoreCase(role)) {
            return "redirect:/user-dashboard#technical-tests";
        }
        return "redirect:/admin-dashboard#technical-tests";
    }
}
