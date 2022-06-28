package com.example.jwt.entity;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "loginid",length = 20, unique = true)
    private String userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name="userRole")
    @Enumerated(value = EnumType.STRING)
    private UserRole UserRole;

    @Column(name = "social_id")
    private long socialId;

    @Column(name="social_profilepic")
    private String profileHref;

}