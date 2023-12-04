package backend.user.service;

import backend.SpringContextRequiringTestBase;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends SpringContextRequiringTestBase {

    @Autowired
    private UserService service;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailTokenService tokenService;

    @Test
    @Order(1)
    public void testCreateUser() {
        String email = "email@student.agh.edu.pl";
        String password = "password";
        String firstName = "name";
        String lastName = "surname";
        boolean isAdmin = false;
        service.createUser(email, password, firstName, lastName, isAdmin);

        User user = userRepository.findUserByEmail(email).get();

        Assertions.assertThat(user.getEmail()).isEqualTo(email);
        Assertions.assertThat(user.getPassword()).isNotNull();
        Assertions.assertThat(user.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(user.getLastName()).isEqualTo(lastName);
        Assertions.assertThat(user.getIsAdmin()).isFalse();
        Assertions.assertThat(user.getIsEmailAuth()).isFalse();
        Assertions.assertThat(user.getIsBanned()).isFalse();
    }

    @Test
    @Order(2)
    public void testConfirmEmail() {
        String email = "email@student.agh.edu.pl";
        User user = userRepository.findUserByEmail(email).get();

        service.confirmEmail(tokenService.generate(user.getId()));

        User confirmedUser = userRepository.findUserByEmail(email).get();

        Assertions.assertThat(confirmedUser.getIsEmailAuth()).isTrue();
    }
}
