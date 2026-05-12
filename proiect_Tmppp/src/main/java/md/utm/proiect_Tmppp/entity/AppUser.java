package md.utm.proiect_Tmppp.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String fullName;
    private String role;
    private String domainPreference;
    private String locationPreference;
    private int experienceYears;
    @Column(length = 4000)
    private String cv;
    @Column(length = 2000)
    private String favoriteJobIds;
    @Column(length = 4000)
    private String notifications;
    @Column(length = 4000)
    private String messages;
    private boolean active = true;

    public AppUser() {}

    public AppUser(String username, String password, String fullName, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
        this.domainPreference = "Technology";
        this.locationPreference = "Chisinau";
        this.cv = "CV not completed yet.";
        this.favoriteJobIds = "";
        this.notifications = "Bun venit pe platforma. Completeaza profilul pentru recomandari mai bune.";
        this.messages = "Recruiter: Salut! Iti putem raspunde aici la intrebari despre joburi.";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getDomainPreference() { return domainPreference; }
    public void setDomainPreference(String domainPreference) { this.domainPreference = domainPreference; }
    public String getLocationPreference() { return locationPreference; }
    public void setLocationPreference(String locationPreference) { this.locationPreference = locationPreference; }
    public int getExperienceYears() { return experienceYears; }
    public void setExperienceYears(int experienceYears) { this.experienceYears = experienceYears; }
    public String getCv() { return cv; }
    public void setCv(String cv) { this.cv = cv; }
    public String getFavoriteJobIds() { return favoriteJobIds; }
    public void setFavoriteJobIds(String favoriteJobIds) { this.favoriteJobIds = favoriteJobIds; }
    public String getNotifications() { return notifications; }
    public void setNotifications(String notifications) { this.notifications = notifications; }
    public String getMessages() { return messages; }
    public void setMessages(String messages) { this.messages = messages; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
