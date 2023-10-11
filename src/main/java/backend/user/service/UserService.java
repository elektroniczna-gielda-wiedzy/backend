package backend.user.service;

import backend.adapter.rest.model.common.UserDto;
import backend.common.service.EmailService;
import backend.common.service.GenericServiceException;
import backend.user.model.ActivityInfo;
import backend.user.model.ExtendedUserDto;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class UserService {
    private final PasswordEncoder encoder;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final ActivityStatisticsService activityStatisticsService;

    public UserService(PasswordEncoder encoder, UserRepository userRepository, EmailService emailService,
                       ActivityStatisticsService activityStatisticsService) {
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.activityStatisticsService = activityStatisticsService;
    }

    public void createUser(String email, String password, String firstname, String lastname, boolean isAdmin) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(firstname);
        user.setLastName(lastname);

        user.setIsActive(true);
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

    public ExtendedUserDto getUserInfo(Integer requestedUserId, Integer requestingUserId) {
        User user = this.userRepository.findById(requestedUserId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", requestedUserId)));

        if (requestingUserId.equals(requestedUserId)) {
            return ExtendedUserDto.builder()
                    .userInfo(UserDto.buildFromModel(user))
                    .email(user.getEmail())
                    .lastLogin(user.getLastLogin())
                    .createdAt(user.getCreatedAt())
                    .activityInfo(activityStatisticsService.getUserActivityInfo(requestedUserId)).build();
        } else {
            return  ExtendedUserDto.builder()
                    .userInfo(UserDto.buildFromModel(user)).build();

        }
    }

}
