package com.planto.fullstackassessment.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class CsvEntry {

    @Id
    @NonNull
    @GeneratedValue()
    private Long id;

    private String data;
}
