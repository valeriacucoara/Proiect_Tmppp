package md.utm.proiect_Tmppp.controller;

import md.utm.proiect_Tmppp.builder.JobListingBuilder;
import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.JobListing;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.repository.CandidateRepository;
import md.utm.proiect_Tmppp.repository.JobListingRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final CandidateRepository candidateRepository;
    private final JobListingRepository jobListingRepository;
    private final AppUserRepository appUserRepository;

    public DataInitializer(CandidateRepository candidateRepository,
                           JobListingRepository jobListingRepository,
                           AppUserRepository appUserRepository) {
        this.candidateRepository = candidateRepository;
        this.jobListingRepository = jobListingRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (candidateRepository.count() == 0) {
            Candidate candidate1 = new Candidate("Andrei Popescu", "andrei.popescu@example.com");
            candidate1.setSkill("Java, Spring");
            candidate1.setStatus("Applied");
            candidate1.setAppliedJobTitle("Software Engineer");
            candidate1.setExpectedSalary(5200);
            candidate1.setCv("Java developer cu experienta in Spring Boot, REST API si SQL.");
            candidateRepository.save(candidate1);

            Candidate candidate2 = new Candidate("Maria Ionescu", "maria.ionescu@example.com");
            candidate2.setSkill("Product Management, Agile");
            candidate2.setStatus("Interviewed");
            candidate2.setAppliedJobTitle("Product Manager");
            candidate2.setExpectedSalary(5800);
            candidate2.setCv("Product manager cu experienta in roadmap, discovery si coordonare Agile.");
            candidateRepository.save(candidate2);
        }

        if (jobListingRepository.count() == 0) {
            JobListingBuilder builder = new JobListingBuilder();
            builder.reset();
            builder.buildTitle("Software Engineer");
            builder.buildDescription("Dezvolta aplicatii web moderne folosind Spring Boot, API-uri REST si baze de date relationale.");
            builder.buildSalary(5200.0);
            JobListing job1 = builder.getResult();
            job1.setDomain("Technology");
            job1.setLocation("Chisinau");
            job1.setExperienceLevel("Mid");
            job1.setApproved(true);
            jobListingRepository.save(job1);

            builder.reset();
            builder.buildTitle("Product Manager");
            builder.buildDescription("Coordoneaza echipe, defineste roadmap-ul produsului si transforma cerintele clientilor in livrari clare.");
            builder.buildSalary(5800.0);
            JobListing job2 = builder.getResult();
            job2.setDomain("Product");
            job2.setLocation("Remote");
            job2.setExperienceLevel("Senior");
            job2.setApproved(true);
            jobListingRepository.save(job2);

            builder.reset();
            builder.buildTitle("Junior QA Analyst");
            builder.buildDescription("Testeaza functionalitati noi, documenteaza defecte si contribuie la calitatea livrarilor.");
            builder.buildSalary(2800.0);
            JobListing job3 = builder.getResult();
            job3.setDomain("Quality Assurance");
            job3.setLocation("Balti");
            job3.setExperienceLevel("Junior");
            job3.setApproved(false);
            jobListingRepository.save(job3);
        }

        if (appUserRepository.count() == 0) {
            AppUser admin = new AppUser("admin", "admin123", "Admin Platforma", "ADMIN");
            admin.setDomainPreference("Technology");
            admin.setLocationPreference("Chisinau");
            admin.setExperienceYears(6);

            AppUser user = new AppUser("user", "user123", "Utilizator Standard", "USER");
            user.setDomainPreference("Technology");
            user.setLocationPreference("Chisinau");
            user.setExperienceYears(2);
            user.setCv("Profil: dezvoltator junior interesat de Java, Spring Boot si aplicatii web.");

            appUserRepository.save(admin);
            appUserRepository.save(user);
        }
    }
}
