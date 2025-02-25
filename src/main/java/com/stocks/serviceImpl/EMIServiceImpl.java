package com.stocks.serviceImpl;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.stocks.entity.EMI;
import com.stocks.entity.Sale;
import com.stocks.exception.ResourceNotFoundException;
import com.stocks.repository.EMIRepository;
import com.stocks.repository.SaleRepository;
import com.stocks.request.EMIRequestDto;
import com.stocks.request.EMIResponseDto;
import com.stocks.service.EMIService;
 

@Service
public class EMIServiceImpl implements EMIService {

    private final EMIRepository emiRepository;
    private final SaleRepository saleRepository;

    public EMIServiceImpl(EMIRepository emiRepository, SaleRepository saleRepository) {
        this.emiRepository = emiRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public EMIResponseDto createEMIPlan(EMIRequestDto emiRequest) {
        Sale sale = saleRepository.findById(emiRequest.getSaleId())
                .orElseThrow(() -> new ResourceNotFoundException("Sale not found"));

        List<LocalDate> dueDates = new ArrayList<>();
        LocalDate startDate = LocalDate.now().plusMonths(1);
        for (int i = 0; i < emiRequest.getMonths(); i++) {
            dueDates.add(startDate.plusMonths(i));
        }

        EMI emi = new EMI();
        
        emi.setSale(sale);
        emi.setDownPayment(emiRequest.getDownPayment());
        emi.setEmiAmount(emiRequest.getEmiAmount());
        emi.setMonths(emiRequest.getMonths());
        emi.setDueDates(dueDates);
        emi.setPaid(false); // Setting false since it's a new EMI

        emi = emiRepository.save(emi);
        return new EMIResponseDto(emi);
    }

    @Override
    public EMIResponseDto getEMIBySaleId(Long saleId) {
        EMI emi = emiRepository.findBySaleId(saleId);
        if (emi == null) {
            throw new ResourceNotFoundException("No EMI plan found for this sale ID");
        }
        return new EMIResponseDto(emi);
    }
}
