package com.example.jwt.service;

import com.example.jwt.dto.UserDto;

public interface SocialUserService {
    void createUser(String access_token);
    String getUserInfoByAccessToken(String access_token);
    UserDto StringToDto(String userInfo);

}
