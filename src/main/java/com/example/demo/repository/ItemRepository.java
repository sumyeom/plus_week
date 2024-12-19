package com.example.demo.repository;

import com.example.demo.entity.Item;
import com.example.demo.entity.Users;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    default Item findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(()->
                        new InvalidDataAccessApiUsageException("해당 ID에 맞는 값이 존재하지 않습니다."));
    }
}
