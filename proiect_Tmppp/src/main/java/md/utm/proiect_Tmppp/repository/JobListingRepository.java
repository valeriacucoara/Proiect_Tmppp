package md.utm.proiect_Tmppp.repository;

import md.utm.proiect_Tmppp.entity.JobListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobListingRepository extends JpaRepository<JobListing, Long> {
    List<JobListing> findByApprovedTrue();
}
