package md.utm.proiect_Tmppp.composite;

import java.util.ArrayList;
import java.util.List;

// Composite: reprezinta o categorie care poate contine mai multe skill-uri individuale.
public class SkillGroup implements SkillComponent {

    private String name;
    private List<SkillComponent> children = new ArrayList<>();

    public SkillGroup() {
        this("Skill Group");
    }

    public SkillGroup(String name) {
        this.name = name;
    }

    public void add(SkillComponent c) {
        children.add(c);
    }

    public void remove(SkillComponent c) {
        children.remove(c);
    }

    public List<SkillComponent> getChildren() {
        return children;
    }

    @Override
    public void execute() {
        System.out.println("Grup de skill-uri:");

        for (SkillComponent child : children) {
            child.execute();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return null;
    }
}
