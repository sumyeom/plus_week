package com.example.demo.service;

import com.example.demo.dto.ItemResponseDto;
import com.example.demo.entity.Item;
import com.example.demo.entity.Users;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ItemService itemService;

    @Test
    void createItem_success() {
        // given
        Long ownerId = 1L;
        Long managerId = 2L;
        String name = "test";
        String description = "test";
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        when(userRepository.findByIdOrElseThrow(ownerId)).thenReturn(owner);
        when(userRepository.findByIdOrElseThrow(managerId)).thenReturn(manager);

        Item item = new Item(name, description, owner, manager);

        when(itemRepository.save(any(Item.class))).thenReturn(item);

        // when
        ItemResponseDto resultDto = itemService.createItem(name,description,ownerId,managerId);

        // then
        assertNotNull(resultDto);
        assertEquals(resultDto.getId(), item.getId());

    }
}