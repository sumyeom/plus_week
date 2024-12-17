package com.example.demo.repository;

import com.example.demo.entity.QItem;
import com.example.demo.entity.QReservation;
import com.example.demo.entity.QUsers;
import com.example.demo.entity.Reservation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ReservationQuerydslRepositoryImpl implements ReservationQuerydslRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Reservation> searchReservationQuerydsl(Long userId, Long itemId) {
        QUsers user = QUsers.users;
        QItem item = QItem.item;
        QReservation reservation = QReservation.reservation;

        BooleanExpression nameCondition = userId != null ? user.id.eq(userId) : null;
        BooleanExpression itemCondition = itemId != null ? item.id.eq(itemId) : null;

        return jpaQueryFactory.selectFrom(reservation)
                .join(reservation.user).fetchJoin()
                .join(reservation.item).fetchJoin()
                .where(nameCondition,itemCondition)
                .fetch();
    }
}
