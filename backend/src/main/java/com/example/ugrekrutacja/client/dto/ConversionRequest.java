package com.example.ugrekrutacja.client.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConversionRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal amountUsd,
        @NotNull @PastOrPresent LocalDate accountingDate
) {}

