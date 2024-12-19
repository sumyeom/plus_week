package com.example.demo.service;

import com.example.demo.constants.Role;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.entity.Users;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.PasswordEncoder;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    void signupWithEmail_success() {
        // given
        UserRequestDto requestDto = new UserRequestDto("user","test@example.com","name","password123");

        String encodedPassword = "encodedPassword123";

        Users users = new Users("user",requestDto.getEmail(),requestDto.getNickname(),encodedPassword);

        when(userRepository.save(any(Users.class))).thenReturn(users);

        // when
        UserResponseDto resultDto = userService.signupWithEmail(requestDto);

        // then
        assertNotNull(resultDto);
        assertEquals(users.getId(), resultDto.getId());
        assertEquals(users.getEmail(), resultDto.getEmail());

        verify(userRepository,times(1)).save(any(Users.class));

    }

    @Test
    void loginUser_success() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com","password123");
        String encodedPassword = PasswordEncoder.encode(requestDto.getPassword());
        Users users = new Users("user","test@example.com","name",encodedPassword);
        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(users);

        // when
        Authentication authentication = userService.loginUser(requestDto);

        // then
        assertNotNull(authentication);
        assertEquals(users.getId(), authentication.getId());
    }

    @Test
    void loginUser_failure() {
        // given
        LoginRequestDto requestDto = new LoginRequestDto("test@example.com","password123");
        String encodedPassword = PasswordEncoder.encode(requestDto.getPassword());
        Users users = new Users("user","test@example.com","name","1234");

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(users);

        // when
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()-> userService.loginUser(requestDto));

        // then
        assertEquals(HttpStatus.UNAUTHORIZED+" \"유효하지 않은 사용자 이름 혹은 잘못된 비밀번호\"",exception.getMessage());
    }
}