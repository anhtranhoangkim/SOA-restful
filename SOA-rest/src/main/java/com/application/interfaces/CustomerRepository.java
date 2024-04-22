package com.application.interfaces;

import com.application.entities.Customer;
import com.application.entities.Export;
import com.application.entities.ExportRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}