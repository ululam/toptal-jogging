package com.toptal.entrance.alexeyz.rest.security.jwt;

import com.toptal.entrance.alexeyz.rest.security.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public class JwtAuthFilter extends GenericFilterBean {
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String AUTH_HEADER_VALUE_START = "Bearer ";

    private UserService userService;

    public JwtAuthFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String token = httpRequest.getHeader(AUTH_HEADER_NAME);
        Authentication auth = null;
        if (token != null && token.startsWith(AUTH_HEADER_VALUE_START)) {
            String jwt = token.replace(AUTH_HEADER_VALUE_START, "");
            String username = TokenUtil.parseUsernameFromToken(jwt);
            auth = userService.loadUserByUsername(username);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}