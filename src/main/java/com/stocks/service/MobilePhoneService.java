package com.stocks.service;

import java.util.List;

import com.stocks.entity.MobilePhone;

public interface MobilePhoneService {
    List<MobilePhone> getAllPhones();
	MobilePhone getPhoneById(Long id);
	MobilePhone updateStock(Long id, int quantity);
	List<MobilePhone> getPhonesByCompany(String company);
    List<MobilePhone> getPhonesByModel(String model);
    List<MobilePhone> findByUser(int user);
    List<MobilePhone> getMyMobiles();
	Object addPhone(MobilePhone mobilePhone);
}
