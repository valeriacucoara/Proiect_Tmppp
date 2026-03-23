package md.utm.proiect_Tmppp.builder;

import md.utm.proiect_Tmppp.entity.JobListing;

public class JobDirector {

    private JobBuilder builder;

    public JobDirector(JobBuilder builder) {
        this.builder = builder;
    }

    public void changeBuilder(JobBuilder builder) {
        this.builder = builder;
    }

    public JobListing make(String type) {

        builder.reset();

        if(type.equals("simple")) {

            builder.buildTitle("Junior Developer");
            builder.buildDescription("Entry level job");
            builder.buildSalary(500);

        } else {

            builder.buildTitle("Senior Developer");
            builder.buildDescription("Experienced job");
            builder.buildSalary(2000);
        }

        return builder.getResult();
    }
}