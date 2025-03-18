package com.example.emailservice.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record UserRegisteredEvent (String name, String email, LocalDateTime createdAt ) implements Serializable {
}
