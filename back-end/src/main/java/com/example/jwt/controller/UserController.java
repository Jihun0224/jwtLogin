//package com.example.jwt.controller;
//
//import com.example.jwt.dto.UserDto;
//import com.example.jwt.entity.User;
//import com.example.jwt.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//import java.util.HashMap;
//import java.util.Optional;
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RestController
//@RequestMapping("/api")
//public class UserController {
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping("/signup")
//    public ResponseEntity<User> signup(
//            @Valid @RequestBody UserDto userDto
//    ) {
//        return ResponseEntity.ok(userService.signup(userDto));
//    }
//
//    @GetMapping("/user")
//    @PreAuthorize("hasAnyRole('USER','ADMIN')")
//    public ResponseEntity<Optional<User>> getMyUserInfo() {
//        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
//    }
//
//    @GetMapping("/user/{username}")
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public ResponseEntity<Optional<User>> getUserInfo(@PathVariable String username) {
//        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
//    }
//    @PostMapping("/kakao")
//    public void  kakaoCallback (@RequestBody HashMap<String, String> param){
//    userService.createKakaoUser(param.get("access_token"));
//    }
//}