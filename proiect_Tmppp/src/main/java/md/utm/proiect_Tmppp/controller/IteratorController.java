package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.iterator.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/iterator")
public class IteratorController {

    @GetMapping("/candidates")
    public String testIterator() {
        CandidateList list = new CandidateList();
        list.addCandidate("Ana");
        list.addCandidate("Ion");
        list.addCandidate("Maria");

        CandidateIterator iterator = list.createIterator();

        StringBuilder result = new StringBuilder();
        while (iterator.hasNext()) {
            result.append(iterator.next()).append("\n");
        }

        return result.toString();
    }
}