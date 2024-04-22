package com.application.interfaces;

import com.application.entities.ExportRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportRowRepository extends JpaRepository<ExportRow, Long> {
    List<ExportRow> findByIdExport(Long idExport);
}