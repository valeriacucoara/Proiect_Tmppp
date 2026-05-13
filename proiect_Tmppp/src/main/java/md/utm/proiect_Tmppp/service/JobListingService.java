package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.JobListing;
import java.util.List;

public interface JobListingService {
    List<JobListing> getAllJobListings();
    JobListing saveJobListing(JobListing jobListing);
    void deleteJobListing(Long id);
  
}