package com.winemarketv2.jwt;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.winemarketv2.DTO.UserCustom;

import io.jsonwebtoken.*;

@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "nghiacubu";

    private final long JWT_EXPIRATION = 604800000L;

    public String generateToken(UserCustom userCustom) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                   .setSubject(userCustom.getUser().getId())
                   .setIssuedAt(now)
                   .setExpiration(expiryDate)
                   .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                   .compact();
    }

    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(JWT_SECRET)
                            .parseClaimsJws(token)
                            .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
