package com.example.demo.repository;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.entity.Item;
import com.example.demo.entity.Users;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ItemRepositoryTest {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdOrElseThrow_success() {
        // given
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        Item item = new Item("test", "description", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);

        // when
        Item findItem = itemRepository.findByIdOrElseThrow(savedItem.getId());

        // then
        assertNotNull(findItem);
        assertEquals(savedItem.getId(), findItem.getId());
    }

    @Test
    void findByIdOrElseThrow_failure() {
        // given
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        userRepository.saveAndFlush(owner);
        userRepository.saveAndFlush(manager);

        Item item = new Item("test1", "description1", manager, owner);
        Item savedItem = itemRepository.saveAndFlush(item);

        // when
        InvalidDataAccessApiUsageException exception =  assertThrows(InvalidDataAccessApiUsageException.class, ()->itemRepository.findByIdOrElseThrow(100L));

        // then
        assertEquals("해당 ID에 맞는 값이 존재하지 않습니다.", exception.getMessage());
    }
}