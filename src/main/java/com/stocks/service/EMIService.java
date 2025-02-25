package com.stocks.service;

import com.stocks.request.EMIRequestDto;
import com.stocks.request.EMIResponseDto;

public interface EMIService {
    EMIResponseDto createEMIPlan(EMIRequestDto emiRequest);
    EMIResponseDto getEMIBySaleId(Long saleId);
}
