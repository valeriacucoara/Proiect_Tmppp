package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    @Autowired
    private JobListingRepository jobListingRepository;

    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    public List<JobListing> getApprovedJobs() {
        return jobListingRepository.findByApprovedTrue();
    }

    public void saveJob(JobListing job) {
        jobListingRepository.save(job);
    }

    public Optional<JobListing> cloneJob(Long id) {
        return jobListingRepository.findById(id)
                .map(original -> {
                    JobListing clone = original.clone();
                    clone.setId(null);
                    return clone;
                });
    }
}
