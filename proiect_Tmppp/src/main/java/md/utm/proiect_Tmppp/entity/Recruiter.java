package md.utm.proiect_Tmppp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "recruiter")
// Concrete Product: utilizator de tip recruiter creat de RecruiterFactory.
public class Recruiter extends User {

    private String company;
    private String specialization;

    public Recruiter() {
        setUserType("Recruiter");
        this.company = "RecruitPro";
        this.specialization = "Technical recruitment";
    }

    public Recruiter(String name, String email) {
        this();
        setName(name);
        setEmail(email);
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public void showRole() {
        System.out.println("Sunt un recrutor.");
    }
}
