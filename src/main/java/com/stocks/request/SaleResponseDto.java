package com.stocks.request;

import java.time.LocalDateTime;

import com.stocks.entity.Sale;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleResponseDto {
    private Long id;
    private String model;
    private int quantitySold;
    private double sellingPrice;
    private String paymentMode;
    private String customerName;
    private LocalDateTime saleDate;

    public SaleResponseDto(Sale sale) {
        this.id = sale.getId();
        this.model = sale.getMobilePhone().getModel();
        this.quantitySold = sale.getQuantitySold();
        this.sellingPrice = sale.getSellingPrice();
        this.paymentMode = sale.getPaymentMode();
        this.customerName = sale.getCustomerName();
        this.saleDate = sale.getSaleDate();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getQuantitySold() {
		return quantitySold;
	}

	public void setQuantitySold(int quantitySold) {
		this.quantitySold = quantitySold;
	}

	public double getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public LocalDateTime getSaleDate() {
		return saleDate;
	}

	public void setSaleDate(LocalDateTime saleDate) {
		this.saleDate = saleDate;
	}
}

