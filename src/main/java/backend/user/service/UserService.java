package backend.user.service;

import backend.common.service.EmailService;
import backend.common.service.GenericServiceException;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class UserService {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

    public UserService(PasswordEncoder encoder,
                       UserRepository userRepository,
                       EmailService emailService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public User getUser(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));
    }

    public List<User> findUserByQuery(String query) {
        if (query == null) {
            throw new GenericServiceException("Query cannot be null");
        }
        return this.userRepository.findUsersByQuery(query.toUpperCase());
    }

    public void createUser(String email, String password, String firstname, String lastname, boolean isAdmin) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstname);
        user.setLastName(lastname);

        user.setIsBanned(false);
        user.setIsEmailAuth(true);  // FIXME
        user.setIsAdmin(isAdmin);
        user.setCreatedAt(Timestamp.from(Instant.now()));

        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

//        this.emailService.sendEmail("mfurga@student.agh.edu.pl", "test", "test:)");
    }
}
