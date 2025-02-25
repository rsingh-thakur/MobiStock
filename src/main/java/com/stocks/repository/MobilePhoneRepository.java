package com.stocks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.stocks.entity.MobilePhone;
import com.stocks.entity.User;

 

public interface MobilePhoneRepository extends JpaRepository<MobilePhone, Long> {

    @Query(value = "SELECT * FROM mobile_phones WHERE LOWER(company) = LOWER(?1)", nativeQuery = true)
    List<MobilePhone> findByCompanyIgnoreCase(String company);
    
    @Query(value = "SELECT * FROM mobile_phones WHERE LOWER(model) = LOWER(?1)", nativeQuery = true)
    List<MobilePhone> findByModelIgnoreCase(String model);
    
    // Fetch Mobile Phones by User ID
    List<MobilePhone> findAllByUserId(int user);
}
