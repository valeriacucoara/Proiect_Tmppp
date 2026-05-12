package md.utm.proiect_Tmppp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import md.utm.proiect_Tmppp.prototype.Prototype;

@Entity
@Table(name = "job_listing")
public class JobListing implements Prototype<JobListing> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String domain;
    private String location;
    private String experienceLevel;
    private double salary;
    private boolean approved = true;

    public JobListing() {
    }

    public JobListing(JobListing prototype) {
        this.title = prototype.title;
        this.description = prototype.description;
        this.domain = prototype.domain;
        this.location = prototype.location;
        this.experienceLevel = prototype.experienceLevel;
        this.salary = prototype.salary;
        this.approved = prototype.approved;
    }

    @Override
    public JobListing clone() {
        return new JobListing(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
