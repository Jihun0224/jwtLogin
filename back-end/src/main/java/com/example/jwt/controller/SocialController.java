package com.example.jwt.controller;

import com.example.jwt.dto.SocialUserDto;
import com.example.jwt.dto.TokenDto;
import com.example.jwt.security.JwtFilter;
import com.example.jwt.security.TokenProvider;
import com.example.jwt.service.KakaoUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api")
public class SocialController {

    Logger logger = LoggerFactory.getLogger(SocialController.class);

    private final KakaoUserService kakaoUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;


    public SocialController(TokenProvider tokenProvider,
                            AuthenticationManagerBuilder authenticationManagerBuilder,
                            KakaoUserService kakaoUserService) {
        this.kakaoUserService=kakaoUserService;
            this.tokenProvider = tokenProvider;
            this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/kakao")
    public ResponseEntity<TokenDto>  kakaoCallback (@RequestBody HashMap<String, String> param){
        SocialUserDto socialUserDto = kakaoUserService.getUserInfoByAccessToken(param.get("access_token"));
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(socialUserDto.getEmail(), socialUserDto.getSocialId());
        logger.debug(authenticationToken.toString());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug(authentication.toString());

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

}