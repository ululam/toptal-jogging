package com.toptal.entrance.alexeyz.rest.security;

import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@Component
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetailsImpl loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        if (user == null)
            return null;

        return new UserDetailsImpl(user);
    }

    public  static class UserDetailsImpl implements UserDetails, Authentication {
        private final User user;

        private List<GrantedAuthority> authorities;

        private boolean authed = true;

        public UserDetailsImpl(User user) {
            this.user = user;

            if (user.isAdmin()) {
                this.authorities = Arrays.asList(GA_USER, GA_MANAGER, GA_ADMIN);
            } else if (user.isManager()) {
                this.authorities = Arrays.asList(GA_USER, GA_MANAGER);
            } else {
                this.authorities = Collections.singletonList(GA_USER);
            }

            this.authorities = Collections.unmodifiableList(this.authorities);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getLogin();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public Object getCredentials() {
            return user.getPassword();
        }

        @Override
        public Object getDetails() {
            return this;
        }

        @Override
        public Object getPrincipal() {
            return user.getLogin();
        }

        @Override
        public boolean isAuthenticated() {
            return authed;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            this.authed = isAuthenticated;
        }

        @Override
        public String getName() {
            return user.getLogin();
        }

    }

    private static final GrantedAuthority GA_ADMIN = new SimpleGrantedAuthority(User.Role.admin.name());
    private static final GrantedAuthority GA_MANAGER = new SimpleGrantedAuthority(User.Role.manager.name());
    private static final GrantedAuthority GA_USER = new SimpleGrantedAuthority(User.Role.user.name());

}
