package md.utm.proiect_Tmppp.service;

import md.utm.proiect_Tmppp.entity.AppUser;
import md.utm.proiect_Tmppp.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final AppUserRepository appUserRepository;

    public UserServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

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
}