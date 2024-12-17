package com.example.demo.entity;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Item Entity에 @DynamicInsert가 존재해 @SpringBootTest로 진행
 */
@SpringBootTest
@Import(QuerydslConfig.class)
class ItemTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("status 값을 넣지 않을 때")
    void testItemWithStatusEmpty_defaultValue(){
        // given
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        // when
        Item item = new Item("test", "description", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);
        Item findItem = itemRepository.findById(savedItem.getId()).get();

        // then
        assertNotNull(findItem);
        assertEquals("PENDING", findItem.getStatus());
    }

    @Test
    @DisplayName("status 값에 Null이 들어갈 때")
    void testItemWithStatusIsNull_defaultValue(){
        // given
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");

        // when
        Item item = new Item("test", "description", manager, owner);
        item.setStatus(null);

        // then
        assertThrows(InvalidDataAccessApiUsageException.class, () -> itemRepository.saveAndFlush(item));
    }
}