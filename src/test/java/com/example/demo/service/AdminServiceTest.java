package com.example.demo.service;

import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    void reportUsers_success() {
        // given
        int updatedCount = 3;
        List<Long> userIds = List.of(1L, 2L, 3L);
        when(userRepository.updateUserStatusBlocked(userIds)).thenReturn(updatedCount);

        // when
        adminService.reportUsers(userIds);

        // then
    }

    @Test
    void reportUsers_failure() {
        // given
        int updatedCount = 3;
        List<Long> userIds = List.of(1L, 2L);
        when(userRepository.updateUserStatusBlocked(userIds)).thenReturn(updatedCount);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> adminService.reportUsers(userIds));

        // then
        assertEquals("업데이트 실패", exception.getMessage());
    }
}