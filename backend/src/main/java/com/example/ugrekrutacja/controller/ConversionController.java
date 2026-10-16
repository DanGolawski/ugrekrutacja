
package com.example.ugrekrutacja.controller;

import jakarta.validation.Valid;
import com.example.ugrekrutacja.client.dto.ConversionBatchRequest;
import com.example.ugrekrutacja.client.dto.ConversionResult;
import com.example.ugrekrutacja.entity.Computer;
import com.example.ugrekrutacja.service.CurrencyConversionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversions")
public class ConversionController {

    private final CurrencyConversionService service;

    public ConversionController(CurrencyConversionService service) {
        this.service = service;
    }

    @PostMapping("/preview")
    public List<ConversionResult> preview(
            @Valid @RequestBody ConversionBatchRequest request
    ) {
        return service.convert(request.computers());
    }

    @PostMapping
    public List<Computer> convertAndSave(
            @Valid @RequestBody ConversionBatchRequest request
    ) {
        return service.convertAndSave(request.computers());
    }
}
