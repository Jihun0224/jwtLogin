package com.example.jwt.dto;

import com.example.jwt.entity.Authority;
import com.example.jwt.entity.User;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Collections;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SocialUserDto {

    @Size(min = 3,max = 50)
    private String nickname;

    @Size(min = 3,max = 50)
    private String email;

    private String password;

    private Long socialId;

    private String profileHref;

    public User toEntity(){
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        return User.builder()
                .socialId(this.socialId)
                .nickname(this.nickname)
                .authorities(Collections.singleton(authority))
                .profileHref(this.profileHref)
                .email(this.email)
                .build();

    }
}
