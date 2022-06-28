package com.example.jwt.security.tokens;

import com.example.jwt.security.UserContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PostAuthorizationToken extends UsernamePasswordAuthenticationToken {

    public PostAuthorizationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

    public static PostAuthorizationToken getTokenFromUserContext(UserContext context){
        return new PostAuthorizationToken(context, context.getPassword(),context.getAuthorities());
    }
}
