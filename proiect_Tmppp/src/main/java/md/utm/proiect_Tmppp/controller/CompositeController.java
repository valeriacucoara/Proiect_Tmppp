package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.composite.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/composite")
public class CompositeController {

    @GetMapping("/skills")
    public String testComposite() {

        SkillLeaf java = new SkillLeaf("Java");
        SkillLeaf spring = new SkillLeaf("Spring");
        SkillLeaf sql = new SkillLeaf("SQL");

        SkillGroup backend = new SkillGroup();
        backend.add(java);
        backend.add(spring);
        backend.add(sql);

        SkillLeaf react = new SkillLeaf("React");

        SkillGroup fullstack = new SkillGroup();
        fullstack.add(backend);
        fullstack.add(react);

        fullstack.execute();

        return "Composite executat (verifica consola)";
    }
}