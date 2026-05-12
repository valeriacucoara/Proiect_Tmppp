package md.utm.proiect_Tmppp.repository;

import md.utm.proiect_Tmppp.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByEmail(String email);
}
