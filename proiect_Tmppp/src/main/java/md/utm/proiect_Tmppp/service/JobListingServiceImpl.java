package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobListingServiceImpl implements JobListingService {
    private final JobListingRepository jobListingRepository;

    public JobListingServiceImpl(JobListingRepository jobListingRepository) {
        this.jobListingRepository = jobListingRepository;
    }

    @Override
    public List<JobListing> getAllJobListings() {
        return jobListingRepository.findAll();
    }

    @Override
    public JobListing saveJobListing(JobListing jobListing) {
        return jobListingRepository.save(jobListing);
    }

    @Override
    public void deleteJobListing(Long id) {
        jobListingRepository.deleteById(id);
    }
}