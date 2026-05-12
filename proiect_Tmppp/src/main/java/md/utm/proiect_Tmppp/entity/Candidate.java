package md.utm.proiect_Tmppp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import md.utm.proiect_Tmppp.state.CandidateState;

@Entity
@Table(name = "candidate")
public class Candidate implements User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String skill;
    private String status;
    private String appliedJobTitle;
    private String recruiterMessage;
    private double expectedSalary;

    @Column(length = 4000)
    private String cv;

    @Transient
    private CandidateState state;

    public Candidate() {
    }

    public Candidate(String name, String email) {
        this.name = name;
        this.email = email;
        this.status = "Applied";
        this.recruiterMessage = "Aplicatia a fost primita si asteapta verificare.";
        this.cv = "CV not provided yet.";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppliedJobTitle() {
        return appliedJobTitle;
    }

    public void setAppliedJobTitle(String appliedJobTitle) {
        this.appliedJobTitle = appliedJobTitle;
    }

    public String getRecruiterMessage() {
        return recruiterMessage;
    }

    public void setRecruiterMessage(String recruiterMessage) {
        this.recruiterMessage = recruiterMessage;
    }

    public double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public CandidateState getState() {
        return state;
    }

    public void setState(CandidateState state) {
        this.state = state;
        if (state != null) {
            this.status = state.getStatusName();
        }
    }

    @Override
    public void showRole() {
        System.out.println("Sunt un candidat.");
    }
}
