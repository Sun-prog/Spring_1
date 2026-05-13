package com.spring.techserv.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record AccountRequestDTO(
        String username,

        String password

){    }
