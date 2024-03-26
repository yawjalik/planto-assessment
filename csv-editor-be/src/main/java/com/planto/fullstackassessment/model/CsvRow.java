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
public class CsvRow {

    @Id
    @NonNull
    @GeneratedValue()
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CsvEntry> csvEntries;
}
