package md.utm.proiect_Tmppp.prototype;

import md.utm.proiect_Tmppp.entity.JobListing;
import java.util.HashMap;
import java.util.Map;

public class JobPrototypeRegistry {

    private Map<String, JobListing> prototypes = new HashMap<>();

    public void addPrototype(String key, JobListing prototype) {
        prototypes.put(key, prototype);
    }

    public JobListing getPrototype(String key) {
        JobListing prototype = prototypes.get(key);
        if (prototype != null) {
            return prototype.clone();
        }
        return null;
    }

    public void initializeDefaults() {
        JobListing juniorJob = new JobListing();
        juniorJob.setTitle("Junior Developer");
        juniorJob.setDescription("Entry level position");
        juniorJob.setSalary(800);
        addPrototype("junior", juniorJob);

        JobListing seniorJob = new JobListing();
        seniorJob.setTitle("Senior Developer");
        seniorJob.setDescription("Experienced position");
        seniorJob.setSalary(2500);
        addPrototype("senior", seniorJob);

        JobListing leadJob = new JobListing();
        leadJob.setTitle("Tech Lead");
        leadJob.setDescription("Leadership position");
        leadJob.setSalary(3500);
        addPrototype("lead", leadJob);
    }
}