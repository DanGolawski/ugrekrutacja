
package com.example.ugrekrutacja.controller;

import com.example.ugrekrutacja.service.ComputerXmlExportService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/computers/export")
public class ComputerExportController {

    private final ComputerXmlExportService exportService;

    public ComputerExportController(
            ComputerXmlExportService exportService
    ) {
        this.exportService = exportService;
    }

    @GetMapping(
            value = "/xml",
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<byte[]> exportXml() {
        byte[] file = exportService.exportAll();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"faktura.xml\""
                )
                .body(file);
    }
}
