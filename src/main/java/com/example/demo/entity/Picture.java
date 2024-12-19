package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "picture")
public class Picture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imagePath;

    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Picture() {
    }

    public Picture(String imagePath, String category, Users user) {
        this.imagePath = imagePath;
        this.category = category;
        this.user = user;
    }
}
