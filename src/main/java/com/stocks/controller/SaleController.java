package com.stocks.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.request.SaleRequestDto;
import com.stocks.request.SaleResponseDto;
import com.stocks.service.SaleService;
 

@RestController
@RequestMapping("/api/sales")
@PreAuthorize("hasRole('SALE') or hasRole('ADMIN')")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/sell")
    public ResponseEntity<SaleResponseDto> sellPhone(@RequestBody SaleRequestDto saleRequest) {
        return ResponseEntity.ok(saleService.sellPhone(saleRequest));
    }

    @GetMapping
    public ResponseEntity<List<SaleResponseDto>> getAllSales() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleResponseDto> getSaleById(@PathVariable Long id) {
        return ResponseEntity.ok(saleService.getSaleById(id));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<SaleResponseDto>> filterSales(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String ramRomVariant,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String paymentMode) {

        List<SaleResponseDto> sales = saleService.getFilteredSales(
                startDate, endDate, company, model, ramRomVariant, color, customerName, paymentMode);
        
        return ResponseEntity.ok(sales);
    }
}

