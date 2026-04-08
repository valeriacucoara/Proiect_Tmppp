package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.proxy.CandidateCvAccess;
import md.utm.proiect_Tmppp.proxy.CandidateCvProxy;
import md.utm.proiect_Tmppp.proxy.RealCandidateCvService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/proxy")
public class ProxyController {

    @GetMapping("/cv")
    public String testProxy(@RequestParam String role) {
        CandidateCvAccess service =
                new CandidateCvProxy(new RealCandidateCvService(), role);

        return service.viewCv();
    }
}