package backend.user.service;

import backend.common.service.GenericServiceException;
import backend.user.model.AppUserDetails;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findUserByEmail(username).orElseThrow(
                () -> new GenericServiceException(String.format("User with email = %s does not exist", username)));
        return new AppUserDetails(user);
    }
}
