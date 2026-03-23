package md.utm.proiect_Tmppp.entity;

import md.utm.proiect_Tmppp.prototype.Prototype;

public class JobListing implements Prototype<JobListing> {

    private String title;
    private String description;
    private double salary;

    public JobListing() {}

    // constructor pentru clonare
    public JobListing(JobListing prototype) {
        this.title = prototype.title;
        this.description = prototype.description;
        this.salary = prototype.salary;
    }

    @Override
    public JobListing clone() {
        return new JobListing(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}