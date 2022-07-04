package com.example.jwt.service;

import com.example.jwt.dto.SocialUserDto;
import com.example.jwt.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class NaverUserService implements SocialUserService{

    private static final Logger logger = LoggerFactory.getLogger(NaverUserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public NaverUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public SocialUserDto getUserInfoByAccessToken(String access_token) {
        String reqURL = "https://openapi.naver.com/v1/nid/me";
        String result = "";
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
            String line;

            while ((line = br.readLine()) != null) {
                result += line;
            }
            logger.debug("response body : " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SocialUserDto userDto = StringToDto(result);
        //일치하는 회원 X -> 가입
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getEmail()).orElse(null) == null) {
            userRepository.save(userDto.toEntity());
        }
        return userDto;
    }
    @Override
    public SocialUserDto StringToDto(String userInfo) {
            SocialUserDto userDto = new SocialUserDto();
            try {
                //JSON 파싱
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = (JSONObject) parser.parse(userInfo);

                JSONObject account = (JSONObject) jsonObj.get("response");

                userDto.setSocialId(account.get("id").toString());
                userDto.setEmail(account.get("email").toString());
                userDto.setNickname(account.get("nickname").toString());
                userDto.setProfileHref(account.get("profile_image").toString());
                userDto.setPassword(passwordEncoder.encode(account.get("id").toString()));
                logger.debug(userDto.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        return userDto;
    }
}
