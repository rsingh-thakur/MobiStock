package com.stocks.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.stocks.entity.Sale;

 


public interface SaleRepository extends JpaRepository<Sale, Long>, JpaSpecificationExecutor<Sale> {
	List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}