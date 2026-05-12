package md.utm.proiect_Tmppp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentRole")
    public String currentRole(HttpSession session) {
        Object role = session.getAttribute("currentRole");
        return role != null ? role.toString() : null;
    }

    @ModelAttribute("currentUser")
    public String currentUser(HttpSession session) {
        Object user = session.getAttribute("currentUser");
        return user != null ? user.toString() : null;
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(HttpSession session) {
        return "ADMIN".equalsIgnoreCase(String.valueOf(session.getAttribute("currentRole")));
    }

    @ModelAttribute("isUser")
    public boolean isUser(HttpSession session) {
        return "USER".equalsIgnoreCase(String.valueOf(session.getAttribute("currentRole")));
    }
}
