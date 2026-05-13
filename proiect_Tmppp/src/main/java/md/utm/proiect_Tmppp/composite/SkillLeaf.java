package md.utm.proiect_Tmppp.composite;

// Leaf: reprezinta un skill individual, de exemplu Java, SQL, HTML sau Communication.
public class SkillLeaf implements SkillComponent {

    private Long id;
    private String name;

    public SkillLeaf(String name) {
        this.name = name;
    }

    public SkillLeaf(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void execute() {
        System.out.println("Skill: " + name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getId() {
        return id;
    }
}
