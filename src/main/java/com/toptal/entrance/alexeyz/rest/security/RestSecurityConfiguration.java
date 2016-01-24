package com.toptal.entrance.alexeyz.rest.security;

import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author alexey.zakharchenko@gmail.com
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserRepository userRepository;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(s -> {
            User user = userRepository.findByLogin(s);
            if (user == null)
                return null;

            return new UserDetailsImpl(user);
        });
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .httpBasic()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.POST, "/rest/jog/**").hasAuthority("user")
                .antMatchers(HttpMethod.DELETE, "/rest/jog/**").hasAuthority("user")
                .antMatchers(HttpMethod.PUT, "/rest/jog/**").hasAuthority("user")
                .antMatchers(HttpMethod.GET, "/rest/jog/**").hasAuthority("user")
                .and()
                .csrf().disable();
    }

    private static class UserDetailsImpl implements UserDetails {
        private final User user;

        private List<GrantedAuthority> authorities;

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
    }

    private static final GrantedAuthority GA_ADMIN = new SimpleGrantedAuthority(User.Role.admin.name());
    private static final GrantedAuthority GA_MANAGER = new SimpleGrantedAuthority(User.Role.manager.name());
    private static final GrantedAuthority GA_USER = new SimpleGrantedAuthority(User.Role.user.name());
}