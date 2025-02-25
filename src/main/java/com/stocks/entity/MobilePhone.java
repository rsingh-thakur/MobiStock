package com.stocks.entity;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "mobile_phones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MobilePhone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String ramRomVariant;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private int stockQuantity;

    @Column(nullable = false)
    private double purchasePrice;

    @Column(nullable = false)
    private double sellingPrice;
    
 
    private int userId;
    

    private String supplierDetails;

    @Column(nullable = true, updatable = false)
    private LocalDateTime dateAdded;

    protected void onCreate() {
        this.dateAdded = LocalDateTime.now();
    }
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getRamRomVariant() {
		return ramRomVariant;
	}

	public void setRamRomVariant(String ramRomVariant) {
		this.ramRomVariant = ramRomVariant;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getSupplierDetails() {
		return supplierDetails;
	}

	public void setSupplierDetails(String supplierDetails) {
		this.supplierDetails = supplierDetails;
	}

	public LocalDateTime getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		this.dateAdded = dateAdded;
	}
	 
    
    
}
