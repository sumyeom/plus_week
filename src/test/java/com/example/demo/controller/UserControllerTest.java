package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.constants.Role;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.UserRequestDto;
import com.example.demo.dto.UserResponseDto;
import com.example.demo.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    Authentication TEST_USER = new Authentication(1L, Role.USER);

    @Test
    void signupWithEmail_success() throws Exception {
        UserRequestDto requestDto = new UserRequestDto("user", "userControllerTest@a.com", "name1", "1234");
        UserResponseDto responseDto = new UserResponseDto(1L, "userControllerTest@a.com", "name1", "1234");

        given(userService.signupWithEmail(requestDto)).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

    }

    @Test
    void loginWithEmail_success() throws Exception {
        LoginRequestDto requestDto = new LoginRequestDto( "userControllerTest@a.com", "1234");

        given(userService.loginUser(requestDto)).willReturn(TEST_USER);

        // when & then
        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk());

    }

    @Test
    void logout_success() throws Exception {
        // when & then
        mockMvc.perform(post("/users/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                        .andExpect(status().isOk());
    }
}