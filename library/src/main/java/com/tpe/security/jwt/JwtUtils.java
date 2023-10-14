package com.tpe.security.jwt;

import com.tpe.security.service.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {


    @Value("${backendapi.app.jwtSecret}")
    private String jwtSecretKey;

    @Value("${backendapi.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);

    //generate Token
    public String generateToken (Authentication authentication){

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userDetails.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512,jwtSecretKey)
                .compact();
    }


    //validate Token
    public boolean validateToken (String token){

        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.error("Secret keyiniz expired olmuştur"); //TODO LOGGER
        } catch (UnsupportedJwtException e) {
            throw new RuntimeException(e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException(e);
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    //get email from Token
    public String getEmailFromToken(String token){
        return Jwts.parser().setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }



}
