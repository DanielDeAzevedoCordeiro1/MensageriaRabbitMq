package com.example.crudservice.dto;

import java.time.LocalDateTime;

public record UserDTO (String name, String email, LocalDateTime createdAt) {
}
