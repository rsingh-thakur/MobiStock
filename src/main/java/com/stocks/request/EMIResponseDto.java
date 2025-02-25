package com.stocks.request;


import java.time.LocalDate;
import java.util.List;

import com.stocks.entity.EMI;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EMIResponseDto {
    private Long id;
    private Long saleId;
    private double downPayment;
    private double emiAmount;
    private int months;
    private List<LocalDate> dueDates;
    private boolean isPaid;

    public EMIResponseDto(EMI emi) {
        this.id = emi.getId();
        this.saleId = emi.getSale().getId();
        this.downPayment = emi.getDownPayment();
        this.emiAmount = emi.getEmiAmount();
        this.months = emi.getMonths();
        this.dueDates = emi.getDueDates();
        this.isPaid = emi.isPaid();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public double getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(double downPayment) {
		this.downPayment = downPayment;
	}

	public double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(double emiAmount) {
		this.emiAmount = emiAmount;
	}

	public int getMonths() {
		return months;
	}

	public void setMonths(int months) {
		this.months = months;
	}

	public List<LocalDate> getDueDates() {
		return dueDates;
	}

	public void setDueDates(List<LocalDate> dueDates) {
		this.dueDates = dueDates;
	}

	public boolean isPaid() {
		return isPaid;
	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}
    
    
}

