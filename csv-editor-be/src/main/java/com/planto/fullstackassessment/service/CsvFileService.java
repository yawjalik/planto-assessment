package com.planto.fullstackassessment.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.planto.fullstackassessment.model.CsvEntry;
import com.planto.fullstackassessment.model.CsvFile;
import com.planto.fullstackassessment.model.CsvIdNameOnly;
import com.planto.fullstackassessment.model.CsvRow;
import com.planto.fullstackassessment.repository.CsvEntryRepository;
import com.planto.fullstackassessment.repository.CsvFileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CsvFileService {
    private final CsvFileRepository fileRepository;
    private final CsvEntryRepository entryRepository;
    private final Logger logger = LogManager.getLogger(CsvFileService.class);

    public List<CsvIdNameOnly> getCsvs() {
        return fileRepository.findAllProjectedBy();
    }

    public Optional<CsvFile> getCsvById(long id) {
        return fileRepository.findById(id);
    }

    public CsvIdNameOnly saveFromFile(MultipartFile file) throws ResponseStatusException {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empty file");
        }
        if (!Objects.equals(file.getContentType(), "text/csv")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid format");
        }

        String filename = file.getOriginalFilename();

        CsvFile newFile = new CsvFile();
        newFile.setFileName(filename);

        try (Reader reader = new InputStreamReader(file.getInputStream())){
            try (CSVReader csvReader = new CSVReader(reader)) {
                List<String[]> list = csvReader.readAll();
                List<CsvRow> csvRows = new ArrayList<>();
                for (String[] row : list) {
                    CsvRow csvRow = new CsvRow();
                    List<CsvEntry> csvEntries = new ArrayList<>();
                    for (String col : row) {
                        CsvEntry csvEntry = new CsvEntry();
                        // remove trailing and leading whitespace
                        csvEntry.setData(col.strip());
                        csvEntries.add(csvEntry);
                    }
                    csvRow.setCsvEntries(csvEntries);
                    csvRows.add(csvRow);
                }
                newFile.setCsvRows(csvRows);
            } catch (CsvException e) {
                logger.error(e);
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read CSV");
            }
        } catch (IOException e) {
            logger.error(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read file");
        }

        fileRepository.save(newFile);

        // Probably a bad idea, is it synchronous?
        Optional<CsvIdNameOnly> returnFile = fileRepository.findProjectedById(newFile.getId());
        if (returnFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File does not exist after save");
        }

        return returnFile.get();
    }

    public CsvFile saveEntries(Long id, List<CsvEntry> csvEntries) throws ResponseStatusException {
        // Check if file exists
        Optional<CsvFile> csvFileOpt = fileRepository.findById(id);
        if (csvFileOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid file ID");
        }

        // TODO: Check if entries belong to file; probably needs bidirectional entity relationship

        CsvFile csvFile = csvFileOpt.get();
        for (CsvEntry entry : csvEntries) {
            Optional<CsvEntry> entryOptional = entryRepository.findById(entry.getId());
            if (entryOptional.isPresent()) {
                CsvEntry entryToUpdate = entryOptional.get();
                entryToUpdate.setData(entry.getData());
                entryRepository.save(entryToUpdate);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Entry to edit not found");
            }
        }
        return csvFile;
    }
}
