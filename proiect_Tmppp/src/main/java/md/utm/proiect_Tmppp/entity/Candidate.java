package md.utm.proiect_Tmppp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import md.utm.proiect_Tmppp.state.CandidateState;

@Entity
@Table(name = "candidate")
// Concrete Product: utilizator de tip candidat creat de CandidateFactory.
public class Candidate extends User {

    private String skill;
    private String status;
    private String appliedJobTitle;

    @Column(length = 1000)
    private String recruiterMessage;
    private double expectedSalary;

    @Column(length = 4000)
    private String cv;

    @Transient
    private CandidateState state;

    public Candidate() {
        setUserType("Candidate");
    }

    public Candidate(String name, String email) {
        setName(name);
        setEmail(email);
        setUserType("Candidate");
        this.status = "Applied";
        this.recruiterMessage = "Aplicatia a fost primita si asteapta verificare.";
        this.cv = "CV not provided yet.";
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
