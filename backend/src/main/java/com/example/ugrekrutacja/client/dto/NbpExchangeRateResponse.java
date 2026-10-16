package com.example.ugrekrutacja.client.dto;

import java.util.List;

public record NbpExchangeRateResponse(
        String table,
        String currency,
        String code,
        List<NbpRate> rates
) {}