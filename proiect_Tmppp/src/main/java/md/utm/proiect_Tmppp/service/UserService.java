package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.AppUser;
import java.util.List;

public interface UserService {
    List<AppUser> getAllUsers();
    AppUser saveUser(AppUser user);
    void deleteUser(Long id);
}