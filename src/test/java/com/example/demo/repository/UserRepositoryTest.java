package com.example.demo.repository;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.entity.Users;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class UserRepositoryTest {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdOrElseThrow_success() {
        Users user = new Users("user","userRepositoryTest@a.com","userRepositoryTest","1234");
        Users savedUser = userRepository.save(user);

        // when
        Users findUser = userRepository.findByIdOrElseThrow(savedUser.getId());

        // then
        assertNotNull(findUser);
        assertEquals(findUser.getEmail(),savedUser.getEmail());
    }

    @Test
    void findByIdOrElseThrow_failure() {
        Users user = new Users("user","userRepositoryTest@a.com","userRepositoryTest","1234");
        Users savedUser = userRepository.save(user);

        // when
        InvalidDataAccessApiUsageException exception = assertThrows(InvalidDataAccessApiUsageException.class , ()-> userRepository.findByIdOrElseThrow(2L));

        // then
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }
}