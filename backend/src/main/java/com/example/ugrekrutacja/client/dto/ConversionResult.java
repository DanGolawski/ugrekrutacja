package com.example.ugrekrutacja.client.dto;


import java.math.BigDecimal;
import java.time.LocalDate;

public record ConversionResult(
        String name,
        LocalDate accountingDate,
        BigDecimal amountUsd,
        BigDecimal exchangeRate,
        LocalDate rateEffectiveDate,
        BigDecimal amountPln
) {}

