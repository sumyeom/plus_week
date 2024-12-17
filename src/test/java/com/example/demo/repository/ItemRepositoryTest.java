package com.example.demo.repository;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.entity.Item;
import com.example.demo.entity.Users;
import org.apache.catalina.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(QuerydslConfig.class)
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("status가 null일 때")
    void testItemWithStatusNull_defaultValue(){
        // given
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        // when
        Item item = new Item("test", "description", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);
        Item findTiem = itemRepository.findById(savedItem.getId()).get();

        // then
        assertNotNull(findTiem);
        assertEquals("PENDING", findTiem.getStatus());
    }
}