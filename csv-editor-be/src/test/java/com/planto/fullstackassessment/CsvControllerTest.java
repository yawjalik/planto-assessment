package com.planto.fullstackassessment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planto.fullstackassessment.controller.CsvController;
import com.planto.fullstackassessment.model.CsvEntry;
import com.planto.fullstackassessment.model.CsvFile;
import com.planto.fullstackassessment.model.CsvIdNameOnly;
import com.planto.fullstackassessment.service.CsvFileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CsvController.class)
public class CsvControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CsvFileService service;

    ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @Test
    void testGetCsvs() throws Exception {
        CsvIdNameOnly proj1 = factory.createProjection(CsvIdNameOnly.class);
        proj1.setId(1L);
        proj1.setFileName("proj1.csv");

        CsvIdNameOnly proj2 = factory.createProjection(CsvIdNameOnly.class);
        proj2.setId(2L);
        proj2.setFileName("proj2.csv");

        List<CsvIdNameOnly> allProjections = List.of(proj1, proj2);

        given(service.getCsvs()).willReturn(allProjections);

        mockMvc.perform(get("/v1/api/csv").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$", hasSize(2)),
                        jsonPath("$[0].fileName").value(proj1.getFileName()),
                        jsonPath("$[1].fileName").value(proj2.getFileName())
                );
    }

    @Test
    void testGetCsvById() throws Exception {
        CsvFile file1 = new CsvFile(1L, "file1.csv");

        given(service.getCsvById(1L)).willReturn(Optional.of(file1));

        mockMvc.perform(get("/v1/api/csv/1").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.fileName").value(file1.getFileName())
                );
    }

    @Test
    void testUploadCsv() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("testfiles/test.csv");
        String content = new String(Objects.requireNonNull(inputStream).readAllBytes());

        MockMultipartFile inputFile = new MockMultipartFile("file", "test.csv", "text/csv", content.getBytes());

        CsvIdNameOnly outputFile = factory.createProjection(CsvIdNameOnly.class);
        outputFile.setFileName("test.csv");

        given(service.saveFromFile(inputFile)).willReturn(outputFile);

        mockMvc.perform(multipart(HttpMethod.POST, "/v1/api/csv")
                .file(inputFile)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.fileName").value(outputFile.getFileName())
                );
    }

    @Test
    void testUpdateCsv() throws Exception {
        CsvEntry newEntry1 = new CsvEntry(1L, "New data");
        List<CsvEntry> newEntries = List.of(newEntry1);

        ObjectMapper objectMapper = new ObjectMapper();

        // Technically can't verify if updated fields are correct in this test.
        // Needs test cases  for CsvFileService
        mockMvc.perform(
                put("/v1/api/csv/1")
                        .content(objectMapper.writeValueAsString(newEntries))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpectAll(
                        status().isNoContent()
                );
    }
}
