package com.stocks.service;

import java.time.LocalDate;
import java.util.List;

import com.stocks.request.SaleRequestDto;
import com.stocks.request.SaleResponseDto;


public interface SaleService {
    SaleResponseDto sellPhone(SaleRequestDto saleRequest);
    List<SaleResponseDto> getAllSales();
    SaleResponseDto getSaleById(Long id);
    
    List<SaleResponseDto> getFilteredSales(
            LocalDate startDate, LocalDate endDate, 
            String company, String model, String ramRomVariant, String color,
            String customerName, String paymentMode);
}
