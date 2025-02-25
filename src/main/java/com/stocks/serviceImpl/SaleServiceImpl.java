package com.stocks.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.stocks.entity.MobilePhone;
import com.stocks.entity.Sale;
import com.stocks.exception.InsufficientStockException;
import com.stocks.exception.ResourceNotFoundException;
import com.stocks.repository.MobilePhoneRepository;
import com.stocks.repository.SaleRepository;
import com.stocks.request.SaleRequestDto;
import com.stocks.request.SaleResponseDto;
import com.stocks.service.SaleService;
import com.stocks.util.SaleSpecification;



@Service
public class SaleServiceImpl implements SaleService {

	private final SaleRepository saleRepository;
	private final MobilePhoneRepository mobilePhoneRepository;

	public SaleServiceImpl(SaleRepository saleRepository, MobilePhoneRepository mobilePhoneRepository) {
		this.saleRepository = saleRepository;
		this.mobilePhoneRepository = mobilePhoneRepository;
	}

	@Override
	public SaleResponseDto sellPhone(SaleRequestDto saleRequest) {
		MobilePhone phone = mobilePhoneRepository.findById(saleRequest.getPhoneId())
				.orElseThrow(() -> new ResourceNotFoundException("Phone not found"));

		if (phone.getStockQuantity() < saleRequest.getQuantitySold()) {
			throw new InsufficientStockException("Insufficient stock available for this sale");
		}

		phone.setStockQuantity(phone.getStockQuantity() - saleRequest.getQuantitySold());
		mobilePhoneRepository.save(phone);

		Sale sale = new Sale(); // Create an instance of Sale

		sale.setMobilePhone(phone);
		sale.setQuantitySold(saleRequest.getQuantitySold());
		sale.setSellingPrice(saleRequest.getSellingPrice());
		sale.setPaymentMode(saleRequest.getPaymentMode());
		sale.setCustomerName(saleRequest.getCustomerName());
		sale.setCustomerPhone(saleRequest.getCustomerPhone());
		sale.setInvoiceUrl(saleRequest.getInvoiceUrl());

		sale = saleRepository.save(sale); // Save the Sale object to the database
		return new SaleResponseDto(sale); 
	}

	@Override
	public List<SaleResponseDto> getAllSales() {
		return saleRepository.findAll().stream().map(SaleResponseDto::new).collect(Collectors.toList());
	}

	@Override
	public SaleResponseDto getSaleById(Long id) {
		Sale sale = saleRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Sale not found with ID: " + id));
		return new SaleResponseDto(sale);
	}
	
	@Override
    public List<SaleResponseDto> getFilteredSales(
            LocalDate startDate, LocalDate endDate, 
            String company, String model, String ramRomVariant, String color,
            String customerName, String paymentMode) {

        LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
        LocalDateTime endDateTime = endDate != null ? endDate.atTime(23, 59, 59) : null;

        Specification<Sale> spec = SaleSpecification.filterSales(
                startDateTime, endDateTime, company, model, ramRomVariant, color, customerName, paymentMode);

        return saleRepository.findAll(spec).stream()
                .map(SaleResponseDto::new)
                .collect(Collectors.toList());
    }

}
