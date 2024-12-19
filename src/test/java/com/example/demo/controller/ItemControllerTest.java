package com.example.demo.controller;

import com.example.demo.constants.GlobalConstants;
import com.example.demo.constants.Role;
import com.example.demo.dto.Authentication;
import com.example.demo.dto.ItemRequestDto;
import com.example.demo.dto.ItemResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Users;
import com.example.demo.service.ItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.support.WebContentGenerator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    Authentication TEST_USER = new Authentication(1L, Role.USER);

    @Test
    void createItem_success() throws Exception {
        Long ownerId = 1L;
        Long managerId = 2L;
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");

        Item item = new Item("test1", "description1", manager, owner);
        ItemRequestDto requestDto = new ItemRequestDto("name","description",ownerId,managerId);
        ItemResponseDto responseDto = new ItemResponseDto(1l,"name","decription",managerId,ownerId);

        when(itemService.createItem(requestDto.getName(),requestDto.getDescription(),requestDto.getOwnerId(),requestDto.getManagerId())).thenReturn(responseDto);

        // when & then
        mockMvc.perform(post("/items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .sessionAttr(GlobalConstants.USER_AUTH, TEST_USER))
                .andExpect(status().isCreated());

    }
}