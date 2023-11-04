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

import static backend.user.model.User.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class UserService {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final EmailTokenService emailTokenService;

    public UserService(PasswordEncoder encoder,
                       UserRepository userRepository,
                       EmailService emailService,
                       EmailTokenService emailTokenService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.emailTokenService = emailTokenService;
    }

    public User getUser(Integer userId) {
        return this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));
    }

    public List<User> findUserByQuery(String query, Boolean isBanned, Boolean isAuth) {
        return this.userRepository.findAll(where(
                matchesQuery(query)
                .and(hasIsBanned(isBanned))
                .and(hasIsEmailAuth(isAuth))
        ));
    }

    public void createUser(String email, String password, String firstname, String lastname, boolean isAdmin) {
        if(this.userRepository.findUserByEmail(email).isPresent()) {
            throw new GenericServiceException(String.format("User with email = %s already exists", email));
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstname);
        user.setLastName(lastname);

        user.setIsBanned(false);
        user.setIsEmailAuth(false);
        user.setIsAdmin(isAdmin);
        user.setCreatedAt(Timestamp.from(Instant.now()));

        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        this.sendEmailConfirmation(user.getId(), user.getEmail());
    }

    public void confirmEmail(String token) {
        if (!this.emailTokenService.verify(token)) {
            throw new GenericServiceException("Invalid email confirmation token");
        }

        Integer userId = this.emailTokenService.getUserId(token);

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (user.getIsEmailAuth()) {
            throw new GenericServiceException("Email already confirmed");
        }

        user.setIsEmailAuth(true);

        try {
            userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }

    public void resendEmail(String email) {
        User user = this.userRepository.findUserByEmail(email).orElseThrow(
                () -> new GenericServiceException(String.format("User with email = %s does not exist", email)));
        this.sendEmailConfirmation(user.getId(), user.getEmail());
    }

    private void sendEmailConfirmation(Integer userId, String email) {
        String token = this.emailTokenService.generate(userId);
        String url = "http://20.251.9.203/email-confirm?token=" + token;
        String message = "Confirm your email address at: " + url;
        this.emailService.sendEmail(email, "Email confirmation", message);
    }

    public void setUserBanned(Integer requestingUser, Integer userId, Boolean isBanned) {
        if (requestingUser.equals(userId)) {
            throw new GenericServiceException("Cannot ban yourself");
        }

        User reqUser = this.userRepository.findById(requestingUser).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", requestingUser)));

        if (!reqUser.getIsAdmin()) {
            throw new GenericServiceException(String.format("User with id = %d is not an admin", requestingUser));
        }

        User bannedUser = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (bannedUser.getIsAdmin() && isBanned) {
            throw new GenericServiceException(String.format("User with id = %d is an admin", userId));
        }

        bannedUser.setIsBanned(isBanned);
        this.userRepository.save(bannedUser);
    }

    public void resetPassword(Integer userId, String oldPassword, String newPassword) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!encoder.matches(oldPassword, user.getPassword())) {
            throw new GenericServiceException("Old password does not match the database value");
        }

        user.setPassword(encoder.encode(newPassword));
        try {
            this.userRepository.save(user);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
