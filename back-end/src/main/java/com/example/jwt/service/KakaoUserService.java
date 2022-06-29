package com.example.jwt.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


import com.example.jwt.dto.UserDto;
import com.example.jwt.repository.UserRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class KakaoUserService implements SocialUserService{
    private static final Logger logger = LoggerFactory.getLogger(KakaoUserService.class);
    private final UserRepository userRepository;

    public KakaoUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUser(String access_token) {
        String userInfo = getUserInfoByAccessToken(access_token);
        UserDto userDto = StringToDto(userInfo);

        //일치하는 회원 X -> 가입 후 로그인 처리

        //일치하는 회원 O -> 로그인 처리

    }

    @Override
    public String getUserInfoByAccessToken(String access_token) {
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
        return result;
    }
        @Override
    public UserDto StringToDto(String userInfo) {
            UserDto userDto = new UserDto();
            try {
                //JSON 파싱
                JSONParser parser = new JSONParser();
                JSONObject jsonObj = new JSONObject();
                jsonObj = (JSONObject) parser.parse(userInfo);

                JSONObject kakao_account = (JSONObject) jsonObj.get("kakao_account");
                JSONObject profile =(JSONObject) kakao_account.get("profile");

                userDto.setUsername(kakao_account.get("email").toString());
                userDto.setUsername(profile.get("nickname").toString());
                logger.debug(userDto.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return userDto;
    }
}
