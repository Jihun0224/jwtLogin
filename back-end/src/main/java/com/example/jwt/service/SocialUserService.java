package com.example.jwt.service;

import com.example.jwt.dto.SocialUserDto;

public interface SocialUserService {
    SocialUserDto getUserInfoByAccessToken(String access_token);
    SocialUserDto StringToDto(String userInfo);

}
