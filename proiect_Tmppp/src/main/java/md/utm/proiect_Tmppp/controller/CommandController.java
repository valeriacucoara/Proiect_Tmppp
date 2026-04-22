package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.command.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/command")
public class CommandController {

    @GetMapping("/run")
    public String testCommand(@RequestParam String action, @RequestParam String candidateName) {
        CandidateReceiver receiver = new CandidateReceiver();
        RecruiterInvoker invoker = new RecruiterInvoker();

        if (action.equalsIgnoreCase("approve")) {
            invoker.setCommand(new ApproveCandidateCommand(receiver, candidateName));
        } else if (action.equalsIgnoreCase("reject")) {
            invoker.setCommand(new RejectCandidateCommand(receiver, candidateName));
        } else {
            invoker.setCommand(new ScheduleInterviewCommand(receiver, candidateName));
        }

        return invoker.runCommand();
    }
}