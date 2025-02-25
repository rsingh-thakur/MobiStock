package com.stocks.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.request.EMIRequestDto;
import com.stocks.request.EMIResponseDto;
import com.stocks.service.EMIService;

 
@RestController
@RequestMapping("/api/emi")
@PreAuthorize("hasRole('EMI') or hasRole('ADMIN')")
public class EMIController {

    private final EMIService emiService;

    public EMIController(EMIService emiService) {
        this.emiService = emiService;
    }

    @PostMapping("/create")
    public ResponseEntity<EMIResponseDto> createEMIPlan(@RequestBody EMIRequestDto emiRequest) {
        return ResponseEntity.ok(emiService.createEMIPlan(emiRequest));
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<EMIResponseDto> getEMIBySaleId(@PathVariable Long saleId) {
        return ResponseEntity.ok(emiService.getEMIBySaleId(saleId));
    }
}

