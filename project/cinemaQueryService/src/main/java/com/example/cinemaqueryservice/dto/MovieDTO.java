package com.example.cinemaqueryservice.dto;

public record MovieDTO(Long id, String title, String description, String imageUrl, int durationMinutes) {}
