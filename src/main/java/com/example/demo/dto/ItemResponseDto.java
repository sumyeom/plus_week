package com.example.demo.dto;

import lombok.Getter;

@Getter
public class ItemResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long managerId;
    private Long ownerId;

    public ItemResponseDto(Long id, String name, String description, Long managerId, Long ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
        this.ownerId = ownerId;
    }
}
