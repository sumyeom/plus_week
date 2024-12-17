package com.example.demo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderTest {

    @Test
    @DisplayName("비밀번호 인코딩")
    void encode_passwordEncoding() {
        String rawPassword = "testPassword";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
    }

    @Test
    @DisplayName("비밀번호 일치")
    void matches_correctPassword() {
        String rawPassword = "testPassword";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        Boolean matches = PasswordEncoder.matches(rawPassword,encodedPassword);

        assertTrue(matches);

    }

    @Test
    @DisplayName("비밀번호 불일치")
    void matches_incorrectPassword() {
        String rawPassword = "testPassword";
        String incorrectPassword = "incorrectPassword";
        String encodedPassword = PasswordEncoder.encode(rawPassword);

        Boolean matches = PasswordEncoder.matches(incorrectPassword,encodedPassword);

        assertFalse(matches);

    }
}