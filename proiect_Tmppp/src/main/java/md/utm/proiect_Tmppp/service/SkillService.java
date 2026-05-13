package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.composite.SkillGroup;
import md.utm.proiect_Tmppp.entity.Skill;

import java.util.List;

public interface SkillService {
    Skill addSkill(String ownerUsername, String category, String name);
    void deleteSkill(Long id, String ownerUsername);
    List<Skill> getSkills(String ownerUsername);
    List<SkillGroup> getSkillGroups(String ownerUsername);
}
