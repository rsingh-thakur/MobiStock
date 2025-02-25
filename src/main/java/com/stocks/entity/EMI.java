package com.stocks.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "emi_plans")
public class EMI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @Column(nullable = false)
    private double downPayment;

    @Column(nullable = false)
    private double emiAmount;

    @Column(nullable = false)
    private int months;

    @ElementCollection
    private List<LocalDate> dueDates;

    @Column(nullable = false)
    private boolean isPaid;

    // Default Constructor
    public EMI() {}

    // Parameterized Constructor
    public EMI(Sale sale, double downPayment, double emiAmount, int months, List<LocalDate> dueDates, boolean isPaid) {
        this.sale = sale;
        this.downPayment = downPayment;
        this.emiAmount = emiAmount;
        this.months = months;
        this.dueDates = dueDates;
        this.isPaid = isPaid;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Sale getSale() { return sale; }
    public void setSale(Sale sale) { this.sale = sale; }

    public double getDownPayment() { return downPayment; }
    public void setDownPayment(double downPayment) { this.downPayment = downPayment; }

    public double getEmiAmount() { return emiAmount; }
    public void setEmiAmount(double emiAmount) { this.emiAmount = emiAmount; }

    public int getMonths() { return months; }
    public void setMonths(int months) { this.months = months; }

    public List<LocalDate> getDueDates() { return dueDates; }
    public void setDueDates(List<LocalDate> dueDates) { this.dueDates = dueDates; }

    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean isPaid) { this.isPaid = isPaid; }

    // Manual Builder Pattern
    public static class Builder {
        private Sale sale;
        private double downPayment;
        private double emiAmount;
        private int months;
        private List<LocalDate> dueDates;
        private boolean isPaid;

        public Builder sale(Sale sale) {
            this.sale = sale;
            return this;
        }

        public Builder downPayment(double downPayment) {
            this.downPayment = downPayment;
            return this;
        }

        public Builder emiAmount(double emiAmount) {
            this.emiAmount = emiAmount;
            return this;
        }

        public Builder months(int months) {
            this.months = months;
            return this;
        }

        public Builder dueDates(List<LocalDate> dueDates) {
            this.dueDates = dueDates;
            return this;
        }

        public Builder isPaid(boolean isPaid) {
            this.isPaid = isPaid;
            return this;
        }

        public EMI build() {
            return new EMI(sale, downPayment, emiAmount, months, dueDates, isPaid);
        }
    }
}
