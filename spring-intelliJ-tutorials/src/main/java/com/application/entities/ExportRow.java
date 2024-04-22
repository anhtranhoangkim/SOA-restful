package com.application.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ExportRows")
public class ExportRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "ExportPrice", nullable = false)
    private float exportPrice;

    @Column(name = "TotalPrice", nullable = false)
    private float totalPrice;

    @Column(name = "IdProduct", nullable = false)
    private int idProduct;

    @Column(name = "IdExport", nullable = false)
    private int idExport;

    @Column(name = "IdCustomer", nullable = false)
    private int idCustomer;

    // Getters and setters...

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getExportPrice() {
        return exportPrice;
    }

    public void setExportPrice(float exportPrice) {
        this.exportPrice = exportPrice;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getIdExport() {
        return idExport;
    }

    public void setIdExport(int idExport) {
        this.idExport = idExport;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }
}

