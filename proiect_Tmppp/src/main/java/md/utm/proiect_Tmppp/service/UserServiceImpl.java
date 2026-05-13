package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.Candidate;
import md.utm.proiect_Tmppp.entity.User;
import md.utm.proiect_Tmppp.factory.method.CandidateFactory;
import md.utm.proiect_Tmppp.factory.method.RecruiterFactory;
import md.utm.proiect_Tmppp.factory.method.UserFactory;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import md.utm.proiect_Tmppp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    @Override
    public AppUser createAccount(String email, String password, String fullName, String role) {
        AppUser account = new AppUser(normalizeEmail(email), password, fullName, role);
        return appUserRepository.save(account);
    }

    @Override
    public User createPlatformUser(String userType, String fullName, String email) {
        UserFactory factory = "recruiter".equalsIgnoreCase(userType)
                ? new RecruiterFactory()
                : new CandidateFactory();
        User user = factory.createUser();
        user.setName(fullName);
        user.setEmail(normalizeEmail(email));
        user.setUserType("recruiter".equalsIgnoreCase(userType) ? "Recruiter" : "Candidate");

        if (user instanceof Candidate candidate) {
            candidate.setStatus("Applied");
            candidate.setSkill("N/A");
            candidate.setRecruiterMessage("Cont creat prin Factory Method. Completeaza profilul pentru aplicari.");
            candidate.setCv("CV not provided yet.");
        }

        return userRepository.save(user);
    }

    @Override
    public boolean platformUserExists(String email) {
        return userRepository.findByEmailIgnoreCase(normalizeEmail(email)).isPresent();
    }

    private String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase();
    }
}
