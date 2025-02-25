package com.stocks.util;


import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.stocks.entity.Sale;

import jakarta.persistence.criteria.Predicate;

public class SaleSpecification {
    
    public static Specification<Sale> filterSales(
            LocalDateTime startDate, LocalDateTime endDate, 
            String company, String model, String ramRomVariant, String color,
            String customerName, String paymentMode) {
        
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (startDate != null && endDate != null) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.between(root.get("saleDate"), startDate, endDate));
            }

            if (company != null && !company.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("mobilePhone").get("company"), company));
            }

            if (model != null && !model.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("mobilePhone").get("model"), model));
            }

            if (ramRomVariant != null && !ramRomVariant.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("mobilePhone").get("ramRomVariant"), ramRomVariant));
            }

            if (color != null && !color.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("mobilePhone").get("color"), color));
            }

            if (customerName != null && !customerName.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.like(root.get("customerName"), "%" + customerName + "%"));
            }

            if (paymentMode != null && !paymentMode.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, 
                    criteriaBuilder.equal(root.get("paymentMode"), paymentMode));
            }

            return predicate;
        };
    }
}
