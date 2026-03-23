package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.JobListing;

public class JobListingBuilder implements JobBuilder {

    private JobListing job;

    public JobListingBuilder() {
        reset();
    }

    @Override
    public void reset() {
        job = new JobListing();
    }

    @Override
    public void buildTitle(String title) {
        job.setTitle(title);
    }

    @Override
    public void buildDescription(String description) {
        job.setDescription(description);
    }

    @Override
    public void buildSalary(double salary) {
        job.setSalary(salary);
    }

    @Override
    public JobListing getResult() {
        return job;
    }
}