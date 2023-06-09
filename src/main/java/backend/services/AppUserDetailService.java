package backend.services;

import backend.model.AppUserDetails;
import backend.model.dao.UserDao;
import backend.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    public AppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDao user = userRepository.findUserDaoByEmail(username);
        if(user == null) {
            throw new UsernameNotFoundException("Email not found");
        }
        return new AppUserDetails(user);
    }
}
