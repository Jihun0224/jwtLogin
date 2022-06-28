package com.example.jwt.security.tokens;

import com.example.jwt.dto.LoginDto;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class PreAuthorizationToken extends UsernamePasswordAuthenticationToken {

    private PreAuthorizationToken(String username, String password){
        super(username,password);
    }
    public PreAuthorizationToken(LoginDto dto){
        super(dto.getUsername(),dto.getPassword());
    }
    public String getUsername(){
        return (String)super.getPrincipal();
    }
    public String getUserPassword(){
        return (String)super.getCredentials();
    }
}
