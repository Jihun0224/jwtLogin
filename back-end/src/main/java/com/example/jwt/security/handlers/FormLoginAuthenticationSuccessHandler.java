package com.example.jwt.security.handlers;

import com.example.jwt.dto.TokenDto;
import com.example.jwt.security.JwtFactory;
import com.example.jwt.security.UserContext;
import com.example.jwt.security.tokens.PostAuthorizationToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//성공 시 동작
//토큰 값을 정형화된 DTO를 response에 내려줌
@Component
public class FormLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtFactory factory;
    private final ObjectMapper objectMapper;

    public FormLoginAuthenticationSuccessHandler(JwtFactory factory,ObjectMapper objectMapper){
        this.factory = factory;
        this.objectMapper = objectMapper;
    }
    //PostAuthorizationToken으로 넘어온 인증 결과값에서 UserContext를 빼내서 UserContext에 묻어있는 유저의 객체정보가 갖고 있는
    //JwtFactory claim set으로 넣어주는 것
    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException, ServletException {
        // To do JWT value on response writer
        PostAuthorizationToken token = (PostAuthorizationToken) authentication;

        UserContext context = (UserContext) token.getPrincipal();

        String tokenString = factory.generateToken(context);

        processResponse(res,writeDto(tokenString));
    }

    private TokenDto writeDto(String token){
        return new TokenDto(token);
    }

    private void processResponse(HttpServletResponse res,TokenDto dto) throws IOException {
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.OK.value());
        res.getWriter().write(objectMapper.writeValueAsString(dto));
    }
}
