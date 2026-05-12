package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterController {

    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model,
                                 HttpSession session) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Parolele nu coincid.");
            return "register";
        }
        if (appUserRepository.findByUsername(email).isPresent()) {
            model.addAttribute("error", "Există deja un cont cu acest email.");
            return "register";
        }
        AppUser newUser = new AppUser(email, password, fullName, "USER");
        appUserRepository.save(newUser);
        session.setAttribute("currentUser", newUser.getFullName());
        session.setAttribute("currentUsername", newUser.getUsername());
        session.setAttribute("currentRole", newUser.getRole());
        return "redirect:/user-dashboard";
    }
}
