package backend.user.service;

import backend.SpringContextRequiringTestBase;
import backend.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AppUserDetailServiceTest extends SpringContextRequiringTestBase {

    @Autowired
    AppUserDetailService service;

    @Test
    public void retrieveInfoTest() {
        User user = getAdminUser();

        UserDetails userDetails = service.loadUserByUsername(user.getEmail());

        Assertions.assertThat(userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)).contains(
                "ADMIN");
        Assertions.assertThat(userDetails.isEnabled()).isTrue();
    }
}
