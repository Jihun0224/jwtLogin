package com.example.jwt.security;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class UserContextService implements UserDetailsService {
    private UserRepository userRepository;
    UserContextService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException{
        User user = userRepository.findByUserId(userId).orElseThrow(()-> new NoSuchElementException("아이디에 맞는 계정이 없습니다"));
        return getUserContext(user);
    }
    private UserContext getUserContext(User user){
        return UserContext.fromUserModel(user);
    }
}
