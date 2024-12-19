package com.example.demo.repository;

import com.example.demo.config.QuerydslConfig;
import com.example.demo.constants.ReservationStatus;
import com.example.demo.entity.Item;
import com.example.demo.entity.Reservation;
import com.example.demo.entity.Users;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(QuerydslConfig.class)
@Transactional
class ReservationQuerydslRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;

    private ReservationQuerydslRepositoryImpl reservationQuerydslRepository;
    LocalDateTime startAt = LocalDateTime.now().minusDays(7);
    LocalDateTime endAt = LocalDateTime.now().plusDays(7);

    @BeforeEach
    void setUp() {
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        reservationQuerydslRepository = new ReservationQuerydslRepositoryImpl(jpaQueryFactory);

        // 데이터 초기화
        Users user1 = new Users("user","a@a.com","name1","1234");
        entityManager.persist(user1);

        Users user2 = new Users("user","b@b.com","name2","1234");
        entityManager.persist(user2);

        Item item1 = new Item("itemName1","description1",user1, user2);
        entityManager.persist(item1);

        Item item2 = new Item("itemName2","description2",user1, user2);
        entityManager.persist(item2);

        Reservation reservation1 = new Reservation(item1, user1, ReservationStatus.PENDING,startAt,endAt);
        entityManager.persist(reservation1);

        Reservation reservation2 = new Reservation(item2, user1, ReservationStatus.PENDING,startAt,endAt);
        entityManager.persist(reservation2);

        entityManager.flush();
    }

    @Test
    void searchReservationQuerydsl_ByUserId() {
        // Given
        Long userId = 1L;
        Long itemId = 1L;

        // When
        List<Reservation> result = reservationQuerydslRepository.searchReservationQuerydsl(userId, itemId);

        // Then
        assertNotNull(result);
        if(result.size()>0){
            assertEquals(result.get(0).getUser().getId(), 1L);
        }

    }
}