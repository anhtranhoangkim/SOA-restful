package com.application.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "ReceiptRows")
public class ReceiptRow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "Quantity", nullable = false)
    private int quantity;

    @Column(name = "ReceiptPrice", nullable = false)
    private float receiptPrice;

    @Column(name = "TotalPrice", nullable = false)
    private float totalPrice;

    @Column(name = "IdProduct", nullable = false)
    private int idProduct;

    @Column(name = "IdReceipt", nullable = false)
    private int idReceipt;

    @Column(name = "IdSupplier", nullable = false)
    private int idSupplier;
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

    public float getReceiptPrice() {
        return receiptPrice;
    }

    public void setReceiptPrice(float receiptPrice) {
        this.receiptPrice = receiptPrice;
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

    public int getIdReceipt() {
        return idReceipt;
    }

    public void setIdReceipt(int idReceipt) {
        this.idReceipt = idReceipt;
    }

    public int getIdSupplier() {
        return idSupplier;
    }

    public void setIdSupplier(int idSupplier) {
        this.idSupplier = idSupplier;
    }
}

