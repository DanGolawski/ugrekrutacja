package com.example.ugrekrutacja.client.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record NbpRate(
        String no,
        LocalDate effectiveDate,
        BigDecimal mid
) {}
