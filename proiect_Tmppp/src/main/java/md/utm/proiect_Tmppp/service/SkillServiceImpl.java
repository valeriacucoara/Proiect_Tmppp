package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.composite.SkillGroup;
import md.utm.proiect_Tmppp.composite.SkillLeaf;
import md.utm.proiect_Tmppp.entity.Skill;
import md.utm.proiect_Tmppp.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public Skill addSkill(String ownerUsername, String category, String name) {
        String safeCategory = category == null || category.isBlank() ? "Other Skills" : category.trim();
        String safeName = name == null || name.isBlank() ? "Skill implicit" : name.trim();
        return skillRepository.save(new Skill(ownerUsername, safeCategory, safeName));
    }

    @Override
    public void deleteSkill(Long id, String ownerUsername) {
        skillRepository.findById(id)
                .filter(skill -> ownerUsername.equals(skill.getOwnerUsername()))
                .ifPresent(skillRepository::delete);
    }

    @Override
    public List<Skill> getSkills(String ownerUsername) {
        return skillRepository.findByOwnerUsernameOrderByCategoryAscNameAsc(ownerUsername);
    }

    @Override
    public List<SkillGroup> getSkillGroups(String ownerUsername) {
        Map<String, SkillGroup> groups = new LinkedHashMap<>();
        for (Skill skill : getSkills(ownerUsername)) {
            groups.computeIfAbsent(skill.getCategory(), SkillGroup::new)
                    .add(new SkillLeaf(skill.getId(), skill.getName()));
        }
        return List.copyOf(groups.values());
    }
}
