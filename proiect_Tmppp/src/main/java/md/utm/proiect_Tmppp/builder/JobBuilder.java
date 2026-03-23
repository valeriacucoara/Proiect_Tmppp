package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.JobListing;

public interface JobBuilder {

    void reset();

    void buildTitle(String title);

    void buildDescription(String description);

    void buildSalary(double salary);

    JobListing getResult();
}