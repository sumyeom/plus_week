package com.example.demo.entity;

import com.example.demo.config.QuerydslConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.request.NativeWebRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
class ItemTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void itemTest(){
        Users owner = new Users("user", "email", "name", "0000");
        Users manager = new Users("user", "email", "name", "0000");
        entityManager.persist(owner);
        entityManager.persist(manager);
        Item item = new Item("test", "description", manager, owner);
        entityManager.persist(item);
        entityManager.flush();

        Item savedItem = entityManager.find(Item.class, item.getId());
        Assertions.assertEquals("PENDING", savedItem.getStatus());
    }
}