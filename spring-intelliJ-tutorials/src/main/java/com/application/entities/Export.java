package com.application.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Exports")
public class Export {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ExportDate", nullable = false)
    private Date exportDate;

    @Column(name = "TotalQuantity", nullable = false)
    private int totalQuantity;

    @Column(name = "TotalPrice", nullable = false)
    private float totalPrice;

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getExportDate() {
        return exportDate;
    }

    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}

