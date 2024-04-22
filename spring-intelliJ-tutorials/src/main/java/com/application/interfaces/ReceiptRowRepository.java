package com.application.interfaces;

import com.application.entities.ReceiptRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceiptRowRepository extends JpaRepository<ReceiptRow, Long> {
    List<ReceiptRow> findByIdReceipt(Long idReceipt);
}