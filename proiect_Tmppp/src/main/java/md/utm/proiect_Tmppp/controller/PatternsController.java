package md.utm.proiect_Tmppp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatternsController {

    @GetMapping("/patterns")
    public String patterns() {
        return "redirect:/";
    }
}
