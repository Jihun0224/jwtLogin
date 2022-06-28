package com.example.jwt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Optional;

import com.example.jwt.dto.UserDto;
import com.example.jwt.entity.Authority;
import com.example.jwt.entity.User;
import com.example.jwt.jwt.JwtFilter;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.util.SecurityUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User signup(UserDto userDto) {
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .isSocial(false)
                .build();

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }

    public void createKakaoUser(String access_token){

        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {

            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //전송할 header 작성
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + access_token);
            conn.setRequestProperty("charset", "UTF-8");
            //결과 확인
            int responseCode = conn.getResponseCode();
            logger.debug("responseCode : " + responseCode);

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result = "";
            String line;

            while((line=br.readLine())!=null){
                result += line;
            }
            logger.debug("response body : " + result);

            //JSON 파싱
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = new JSONObject();
            jsonObj = (JSONObject) parser.parse(result);

            UserDto userDto = new UserDto();
            JSONObject kakao_account = (JSONObject) jsonObj.get("kakao_account");
            JSONObject profile =(JSONObject) kakao_account.get("profile");

            userDto.setUsername(kakao_account.get("email").toString());
            userDto.setNickname(profile.get("nickname").toString());
            logger.debug(userDto.toString());
            //일치하는 회원 X -> 가입 후 로그인 처리
            if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) == null){
                Authority authority = Authority.builder()
                        .authorityName("ROLE_USER")
                        .build();
                User user = User.builder()
                        .username(userDto.getUsername())
                        .nickname(userDto.getNickname())
                        .authorities(Collections.singleton(authority))
                        .activated(true)
                        .isSocial(true)
                        .build();
                userRepository.save(user);
            }
            //일치하는 회원 O -> 로그인 처리
            else{

            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            logger.debug("JSON 변환 실패");
            e.printStackTrace();
        }
    }
}
