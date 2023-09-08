package backend.model;

import backend.model.dao.UserDao;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AppUserDetails implements UserDetails {
    private final UserDao userDao;

    public AppUserDetails(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        if (userDao.getIsAdmin()) {
            authorityList.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorityList.add(new SimpleGrantedAuthority("USER"));
        }
        return authorityList;
    }

    public Integer getId() {
        return userDao.getUserId();
    }

    @Override
    public String getPassword() {
        return userDao.getPassword();
    }

    @Override
    public String getUsername() {
        return userDao.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDao.getIsActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDao.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDao.getIsActive();
    }

    @Override
    public boolean isEnabled() {
        return userDao.getIsActive() && userDao.getIsEmailAuth();
    }
}
