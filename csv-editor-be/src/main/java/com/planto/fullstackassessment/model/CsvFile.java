package com.planto.fullstackassessment.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CsvFile {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String fileName;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CsvRow> csvRows;
}

