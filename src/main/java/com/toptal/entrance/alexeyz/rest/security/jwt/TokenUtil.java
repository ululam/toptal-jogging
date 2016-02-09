package com.toptal.entrance.alexeyz.rest.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author alexey.zakharchenko@gmail.com
 */
public final class TokenUtil {
    private static final String secret = "hihowareyoutoda";

    public static String parseUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public static String createTokenForUser(String username) {
        return Jwts.builder()
                .setSubject(username)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
