package com.electricity.billing.system.controller;

import com.electricity.billing.system.service.BillService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.electricity.billing.system.repository.CustomerRepository; // Import CustomerRepository
import com.electricity.billing.system.dto.CustomerRequestDto;
import com.electricity.billing.system.dto.ErrorResponseDto;
import com.electricity.billing.system.dto.LoginRequestDto;
import com.electricity.billing.system.entity.CustomerModel;
import com.electricity.billing.system.service.CustomerLoginService;
import com.electricity.billing.system.service.CustomerService;
import com.electricity.billing.system.util.Constants;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class CustomerController {

	static final Logger LOGGER = LogManager.getLogger(CustomerController.class);

	@Autowired
	CustomerService service;

	@Autowired
	CustomerLoginService loginService;

	@Autowired // Autowire CustomerRepository
	CustomerRepository customerRepository;

	@Autowired
	private BillService billService;

	@PostMapping("/register")
	public ResponseEntity<?> customerRegister(@RequestBody CustomerRequestDto request) {
		log.info("In CustomerController customerRegister() with request :" + request);
		CustomerModel model = new CustomerModel();
		ErrorResponseDto response = new ErrorResponseDto();

		if (request.getPassword().equals(request.getConfirmPassword())) {
			return service.customerRegister(request);
		} else {
			response.setError_code(Constants.EBS105);
			response.setError_message(Constants.PASSWORD_AND_CONFIRMPASSWORD_NOT_MATCHED);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginRequest(@RequestBody LoginRequestDto request) {
		// Retrieve the user from the database based on the email provided in the request
		CustomerModel customer = customerRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());

		// Check if the user exists in the database and if the password matches
		if (customer != null) {
			// If the credentials are valid, login successful
			return ResponseEntity.ok().body("Login successful");
		} else {
			// If the credentials are invalid, return an error message
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
		}
	}

	@GetMapping("user/login")
	public String LoginPage() {
		return "login.html";
	}

	@GetMapping("user/register")
	public String RegisterPage() {
		return "register.html";
	}

	@GetMapping("/findByMeterNumber")
	public ResponseEntity<?> findCustomerByMeterNumber(@RequestParam String meterNumber) {
		CustomerModel customer = billService.findCustomerByMeterNumber(meterNumber);
		if (customer != null) {
			return ResponseEntity.ok(customer);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found for meter number: " + meterNumber);
		}
	}
	@GetMapping("/api/customers/details")
	public ResponseEntity<CustomerModel> getCustomerDetailsByMeterNumber(@RequestParam("meterNumber") String meterNumber) {
		try {
			CustomerModel customer = billService.findCustomerDetailsByMeterNumber(meterNumber);
			if (customer != null) {
				return ResponseEntity.ok(customer);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@GetMapping("/viewbill")
	public String viewBillPage(){
		return "viewbill.html";
	}

}
