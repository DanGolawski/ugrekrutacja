package com.example.ugrekrutacja.client.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ConversionBatchRequest(
        @NotEmpty(message = "Lista komputerów nie może być pusta")
        List<@Valid ConversionRequest> computers
) {}
