package com.toptal.entrance.alexeyz.rest.security;

import com.toptal.entrance.alexeyz.rest.security.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures RESP API security via Spring autoconfiguraion
 *
 * @author alexey.zakharchenko@gmail.com
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class RestSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(HttpMethod.POST, "/rest/jog/**").hasAuthority("user").
                and()
                    .csrf().disable()
                .addFilterBefore(new JwtAuthFilter(userService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                ;
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return userService;
    }
}