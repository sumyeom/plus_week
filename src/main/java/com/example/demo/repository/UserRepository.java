package com.example.demo.repository;

import com.example.demo.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Users u set u.status = 'BLOCKED' where u.id in :userIds")
    int updateUserStatusBlocked(@Param("userIds") List<Long> userIds);

    default Users findByIdOrElseThrow(Long id) {
        return findById(id)
                .orElseThrow(()->
                        new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
    }
}
