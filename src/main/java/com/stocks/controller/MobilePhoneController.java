package com.stocks.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stocks.entity.MobilePhone;
import com.stocks.entity.User;
import com.stocks.service.MobilePhoneService;
import com.stocks.service.UserService;



@RestController
@RequestMapping("/api/phones")
@PreAuthorize("hasRole('MOBILES') or hasRole('ADMIN')")
public class MobilePhoneController {

 
	private final MobilePhoneService mobilePhoneService;

	
	public MobilePhoneController(MobilePhoneService mobilePhoneService) {
		this.mobilePhoneService = mobilePhoneService;
	}

	// **1. Add a new mobile phone to inventory**
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Object> createMobile(@RequestBody MobilePhone mobilePhone ) {
     
		return ResponseEntity.ok(mobilePhoneService.addPhone(mobilePhone ));
	}

	// **2. Get all mobile phones in inventory**
	@GetMapping 
	@PreAuthorize("hasRole('ADMIN')  or hasRole('ROLE_GET_ALL_PHONES')")
	public ResponseEntity<List<MobilePhone>> getAllPhones() {
		return ResponseEntity.ok(mobilePhoneService.getAllPhones());
	}

	// **3. Get a phone by its ID**
	@GetMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')  or hasRole('ROLE_GET_PHONE')")
	public ResponseEntity<MobilePhone> getPhoneById(@PathVariable Long id) {
		return ResponseEntity.ok(mobilePhoneService.getPhoneById(id));
	}

	// **4. Update stock quantity for a mobile phone**
	@PutMapping("/{id}/update-stock")
	public ResponseEntity<MobilePhone> updateStock(@PathVariable Long id, @RequestParam int quantity) {
		return ResponseEntity.ok(mobilePhoneService.updateStock(id, quantity));
	}

	// **5. Get mobile phones filtered by company name**

	@GetMapping("/filter/company")
	public ResponseEntity<List<MobilePhone>> getPhonesByCompany(@RequestParam String company) {
		return ResponseEntity.ok(mobilePhoneService.getPhonesByCompany(company));
	}

	// **6. Get mobile phones filtered by model name**
	@GetMapping("/filter/model")
	public ResponseEntity<List<MobilePhone>> getPhonesByModel(@RequestParam String model) {
		return ResponseEntity.ok(mobilePhoneService.getPhonesByModel(model));
	}
	
	@GetMapping("/my-phones")
	public ResponseEntity< List<MobilePhone>> getMyPhones( ) {
	    return ResponseEntity.ok(mobilePhoneService.getMyMobiles());
	       
	}

}
