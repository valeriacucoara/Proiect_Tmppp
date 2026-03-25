package md.utm.proiect_Tmppp.composite;

import java.util.ArrayList;
import java.util.List;

public class SkillGroup implements SkillComponent {

    private List<SkillComponent> children = new ArrayList<>();

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
}