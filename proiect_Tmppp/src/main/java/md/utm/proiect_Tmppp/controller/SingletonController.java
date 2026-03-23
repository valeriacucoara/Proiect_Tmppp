package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.singleton.PlatformSettings;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/singleton")
public class SingletonController {

    @GetMapping("/test")
    public String testSingleton() {

        PlatformSettings s1 = PlatformSettings.getInstance();
        PlatformSettings s2 = PlatformSettings.getInstance();

        if (s1 == s2) {
            return "Aceeasi instanta Singleton";
        }

        return "Instante diferite";
    }
}