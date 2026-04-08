package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.flyweight.CandidateTestContext;
import md.utm.proiect_Tmppp.flyweight.TestTypeFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/flyweight")
public class FlyweightController {

    @GetMapping("/tests")
    public String testFlyweight() {
        TestTypeFactory factory = new TestTypeFactory();

        CandidateTestContext c1 = new CandidateTestContext(
                "Java Test",
                "Ana - scor 95",
                factory
        );

        CandidateTestContext c2 = new CandidateTestContext(
                "Java Test",
                "Ion - scor 88",
                factory
        );

        CandidateTestContext c3 = new CandidateTestContext(
                "SQL Test",
                "Maria - scor 79",
                factory
        );

        return c1.operation() + "\n" +
                c2.operation() + "\n" +
                c3.operation() + "\n" +
                "Numar obiecte Flyweight create: " + factory.getCacheSize();
    }
}