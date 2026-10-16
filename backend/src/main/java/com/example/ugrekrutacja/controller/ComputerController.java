
package com.example.ugrekrutacja.controller;

import com.example.ugrekrutacja.entity.Computer;
import com.example.ugrekrutacja.service.ComputerQueryService;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/computers")
public class ComputerController {

    private final ComputerQueryService computerQueryService;

    public ComputerController(ComputerQueryService computerQueryService) {
        this.computerQueryService = computerQueryService;
    }

    @GetMapping
    public Page<Computer> search(
            @RequestParam(required = false) String name,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate accountingDate,

            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return computerQueryService.search(
                name,
                accountingDate,
                sortBy,
                direction,
                page,
                size
        );
    }
}
