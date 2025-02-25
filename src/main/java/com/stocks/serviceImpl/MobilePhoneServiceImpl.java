package com.stocks.serviceImpl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stocks.entity.MobilePhone;
import com.stocks.exception.ResourceNotFoundException;
import com.stocks.repository.MobilePhoneRepository;
import com.stocks.service.MobilePhoneService;
import com.stocks.util.UserUtil;
 

@Service
public class MobilePhoneServiceImpl implements MobilePhoneService {

    private final MobilePhoneRepository phoneRepository;
    
    @Autowired
    private   UserUtil userUtil;

    public MobilePhoneServiceImpl(MobilePhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

   

    @Override
    public List<MobilePhone> getAllPhones() {
        return phoneRepository.findAll();
    }

    @Override
    public MobilePhone getPhoneById(Long id) {
        return phoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Phone not found with ID: " + id));
    }

    @Override
    public MobilePhone updateStock(Long id, int quantity) {
        MobilePhone phone = getPhoneById(id);
        phone.setStockQuantity(quantity);
        return phoneRepository.save(phone);
    }
    
    @Override
    public List<MobilePhone> getPhonesByCompany(String company) {
        return phoneRepository.findByCompanyIgnoreCase(company);
    }

    @Override
    public List<MobilePhone> getPhonesByModel(String model) {
        return phoneRepository.findByModelIgnoreCase(model);
    }

	@Override
	public List<MobilePhone> findByUser(int user) {
		return phoneRepository.findAllByUserId(user) ;
	}

	@Override
	public List<MobilePhone> getMyMobiles( ) {
		int userId = (int) userUtil.getCurrentUser().getId();
		return phoneRepository.findAllByUserId(userId);
		 
	
	}



	@Override
	public Object addPhone(MobilePhone mobilePhone) {
		int userId = (int) userUtil.getCurrentUser().getId();
		mobilePhone.setUserId(userId);
		return phoneRepository.save(mobilePhone);
		  
	}


 
}
