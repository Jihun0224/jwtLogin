package com.example.jwt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtFactory {
    private static final Logger logger = LoggerFactory.getLogger(JwtFactory.class);
    private Key key;

    public JwtFactory(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserContext context){

        String token=null;
        try {
            token= Jwts.builder()
                    .setIssuer("Jihun")
                    .claim("USER_ROLE",context.getUser().getUserRole().getRoleName())
                    .signWith(key,SignatureAlgorithm.HS256)
                    .compact();
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return token;
    }

}
