package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.entity.User;
import java.util.List;

public interface UserService {
    List<AppUser> getAllUsers();
    AppUser saveUser(AppUser user);
    void deleteUser(Long id);
    AppUser createAccount(String email, String password, String fullName, String role);
    User createPlatformUser(String userType, String fullName, String email);
    boolean platformUserExists(String email);
}
