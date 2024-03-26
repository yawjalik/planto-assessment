package com.planto.fullstackassessment.repository;

import com.planto.fullstackassessment.model.CsvEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CsvEntryRepository extends JpaRepository<CsvEntry, Long> {
}
