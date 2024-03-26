package com.planto.fullstackassessment.repository;

import com.planto.fullstackassessment.model.CsvFile;
import com.planto.fullstackassessment.model.CsvIdNameOnly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CsvFileRepository extends JpaRepository<CsvFile, Long> {
    List<CsvIdNameOnly> findAllProjectedBy();

    Optional<CsvIdNameOnly> findProjectedById(long id);
}
