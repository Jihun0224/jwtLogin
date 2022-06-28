package com.example.jwt.security.providers;

import com.example.jwt.entity.User;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.UserContext;
import com.example.jwt.security.UserContextService;
import com.example.jwt.security.tokens.PostAuthorizationToken;
import com.example.jwt.security.tokens.PreAuthorizationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;

public class FormLoginAuthenticationProvider implements AuthenticationProvider {

    private final UserContextService userContextService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    FormLoginAuthenticationProvider(UserContextService userContextService,
                                    UserRepository userRepository,
                                    PasswordEncoder passwordEncoder){
        this.userContextService = userContextService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        PreAuthorizationToken token = (PreAuthorizationToken) authentication;

        String username=token.getUsername();
        String password = token.getUserPassword();

        User user = userRepository.findByUserId(username).orElseThrow(()->new NoSuchElementException("정보에 맞는 계정이 없습니다"));

        if(isCorrectPassword(password,user)){
            return PostAuthorizationToken.getTokenFromUserContext(UserContext.fromUserModel(user));
        }
        throw new NoSuchElementException("인증 정보가 정확하지 않습니다.");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return PreAuthorizationToken.class.isAssignableFrom(aClass);
    }

    //비밀번호 일치 여부
    private boolean isCorrectPassword(String password, User user){
        //순서 주의!(원본, 입력값)
        return passwordEncoder.matches(user.getPassword(),password);
    }
}
