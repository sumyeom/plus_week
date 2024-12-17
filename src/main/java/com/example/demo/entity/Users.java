package com.example.demo.entity;

import com.example.demo.constants.Role;
import com.example.demo.constants.UserStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
@Getter
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status; // NORMAL, BLOCKED

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    public Users(String role, String email, String nickname, String password) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public Users() {}

    public void updateStatusToBlocked() {
        this.status = UserStatus.of("BLOCKED");
    }
}
