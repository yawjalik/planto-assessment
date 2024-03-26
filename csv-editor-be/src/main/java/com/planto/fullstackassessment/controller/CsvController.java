package com.planto.fullstackassessment.controller;

import com.planto.fullstackassessment.model.CsvEntry;
import com.planto.fullstackassessment.model.CsvIdNameOnly;
import com.planto.fullstackassessment.model.CsvFile;
import com.planto.fullstackassessment.service.CsvFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;


@RestController
@RequestMapping("/v1/api/csv")
@RequiredArgsConstructor
public class CsvController {
    private final CsvFileService service;

    @GetMapping()
    @CrossOrigin(origins = {"http://localhost:3000"})
    public List<CsvIdNameOnly> getCsvs() {
        return service.getCsvs();
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = {"http://localhost:3000"})
    public CsvFile getCsvById(@PathVariable long id) throws ResponseStatusException {
        Optional<CsvFile> csvFile = service.getCsvById(id);
        if (csvFile.isPresent()) {
            return csvFile.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found");
        }
    }

    @PostMapping()
    @CrossOrigin(origins = {"http://localhost:3000"})
    @ResponseStatus(HttpStatus.CREATED)
    public CsvIdNameOnly uploadCsv(@RequestParam("file") MultipartFile file) {
        return service.saveFromFile(file);
    }

    @PutMapping("/{id}")
    @CrossOrigin("http://localhost:3000")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCsv(@PathVariable Long id, @RequestBody List<CsvEntry> csvEntries) {
        service.saveEntries(id, csvEntries); // Return result ignored
    }
}
