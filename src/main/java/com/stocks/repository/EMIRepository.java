package com.stocks.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.stocks.entity.EMI;

 
public interface EMIRepository extends JpaRepository<EMI, Long> {
     EMI findBySaleId(Long saleId);
}
