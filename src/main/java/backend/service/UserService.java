package backend.service;

import backend.model.User;
import backend.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserService {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder encoder, UserRepository userRepository) {
        this.encoder = encoder;
        this.userRepository = userRepository;
    }

    public void register(String email, String password, String firstname, String lastname) {

        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstname);
        user.setLastName(lastname);

        // FIXME
        user.setIsEmailAuth(true);
        user.setIsActive(true);
        user.setIsAdmin(false);
        user.setCreatedAt(Timestamp.from(Instant.now()));

        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
