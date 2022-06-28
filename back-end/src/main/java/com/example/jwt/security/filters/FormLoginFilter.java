package com.example.jwt.security.filters;

import com.example.jwt.dto.LoginDto;
import com.example.jwt.security.tokens.PreAuthorizationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//processingURL을 정해놓는 것이 아니므로 컴포넌트로 구현하지 않음
public class FormLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthenticationSuccessHandler authenticationSuccessHandler;
    private AuthenticationFailureHandler authenticationFailureHandler;

    public FormLoginFilter(String defaultFilterProcesseUrl,AuthenticationSuccessHandler authenticationSuccessHandler,AuthenticationFailureHandler authenticationFailureHandler){
        super(defaultFilterProcesseUrl);
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    protected FormLoginFilter(String defaultFilterProcessesUrl){
        super(defaultFilterProcessesUrl);
    }
    //인증 시도 -> Provider를 통해 결과값을 받음
    //Provider의 결과: 성공 -> User, 실패-> Error
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        LoginDto dto = new ObjectMapper().readValue(request.getReader(), LoginDto.class);

        PreAuthorizationToken token = new PreAuthorizationToken(dto);

        return super.getAuthenticationManager().authenticate(token);
    }
    //성공시
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,Authentication authResult) throws ServletException, IOException {
        this.authenticationSuccessHandler.onAuthenticationSuccess(request,response,chain,authResult);
    }
    //실패 시
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException fail) throws ServletException, IOException {
        AuthenticationFailureHandler handler = (req,res,exception)->{
            Logger logger = LoggerFactory.getLogger("Authentication_Failure");
            logger.error(exception.getMessage());
        };
        handler.onAuthenticationFailure(request,response,fail);
    }
}
