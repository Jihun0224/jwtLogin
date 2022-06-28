package com.example.jwt.security;

import com.example.jwt.entity.User;
import com.example.jwt.entity.UserRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtFactoryTest {
    private static final Logger logger = LoggerFactory.getLogger(JwtFactoryTest.class);

    private UserContext context;

    @Autowired
    private JwtFactory factory;

    @Before
    public void setUp(){
        User user = new User();
        this.context = UserContext.fromUserModel(user);
    }

    @Test
    public void TEST_JWT_GENERATE(){
        logger.error(factory.generateToken(this.context));
    }
}