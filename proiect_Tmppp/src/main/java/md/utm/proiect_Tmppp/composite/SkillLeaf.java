package md.utm.proiect_Tmppp.composite;

public class SkillLeaf implements SkillComponent {

    private String name;

    public SkillLeaf(String name) {
        this.name = name;
    }

    @Override
    public void execute() {
        System.out.println("Skill: " + name);
    }
}