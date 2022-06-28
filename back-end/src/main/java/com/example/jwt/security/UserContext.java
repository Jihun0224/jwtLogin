package com.example.jwt.security;

import com.example.jwt.entity.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

//PostAuthorizationToken의 authorities 생성
public class UserContext extends User {

    private com.example.jwt.entity.User user;

    private UserContext(com.example.jwt.entity.User user, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.user=user;
    }
    public static UserContext fromUserModel(com.example.jwt.entity.User user){
        return new UserContext(user, user.getUserId(),user.getPassword(),parseAuthorities(user.getUserRole()));
    }
    private static List<SimpleGrantedAuthority>parseAuthorities(UserRole role){
        return Arrays.asList(role).stream().map(r->new SimpleGrantedAuthority(r.getRoleName())).collect(Collectors.toList());
    }
    public com.example.jwt.entity.User getUser(){
        return user;
    }
}
