package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.bridge.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bridge")
public class BridgeController {

    @GetMapping("/report")
    public String testBridge() {

        TestReport report1 = new DetailedTestReport(new EmailSender());
        report1.generateReport("Ana", 90);

        TestReport report2 = new DetailedTestReport(new DashboardSender());
        report2.generateReport("Ion", 70);

        return "Bridge executat (verifica consola)";
    }
}