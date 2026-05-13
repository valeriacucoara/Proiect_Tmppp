package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.User;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String userType,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model,
                                 HttpSession session) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Parolele nu coincid.");
            return "register";
        }
        if (appUserRepository.findByUsernameIgnoreCase(normalizedEmail).isPresent()) {
            model.addAttribute("error", "Exista deja un cont cu acest email.");
            return "register";
        }
        if (userService.platformUserExists(normalizedEmail)) {
            model.addAttribute("error", "Exista deja un utilizator al platformei cu acest email.");
            return "register";
        }

        User createdUser = userService.createPlatformUser(userType, fullName, normalizedEmail);
        String role = "recruiter".equalsIgnoreCase(userType) ? "RECRUITER" : "CANDIDATE";
        AppUser newUser = userService.createAccount(normalizedEmail, password, fullName, role);

        session.setAttribute("currentUser", newUser.getFullName());
        session.setAttribute("currentUsername", newUser.getUsername());
        session.setAttribute("currentRole", newUser.getRole());
        session.setAttribute("registerMessage", "Utilizator creat cu Factory Method: " + createdUser.getUserType());
        session.setAttribute("createdPlatformUserName", createdUser.getName());
        session.setAttribute("createdPlatformUserEmail", createdUser.getEmail());
        return "redirect:/user-dashboard";
    }
}
