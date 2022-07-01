package com.example.jwt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.example.jwt.dto.SocialUserDto;
import com.example.jwt.dto.UserDto;
import com.example.jwt.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class KakaoUserService implements SocialUserService{
    private static final Logger logger = LoggerFactory.getLogger(KakaoUserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public KakaoUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public SocialUserDto getUserInfoByAccessToken(String access_token) {
        String reqURL = "https://kapi.kakao.com/v2/user/me";
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

                JSONObject kakao_account = (JSONObject) jsonObj.get("kakao_account");
                JSONObject profile =(JSONObject) kakao_account.get("profile");

                userDto.setSocialId(Long.valueOf((jsonObj.get("id").toString())));
                userDto.setEmail(kakao_account.get("email").toString());
                userDto.setNickname(profile.get("nickname").toString());
                userDto.setProfileHref(profile.get("profile_image_url").toString());
                userDto.setPassword(passwordEncoder.encode(jsonObj.get("id").toString()));
                logger.debug(userDto.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }
        return userDto;
    }
}
