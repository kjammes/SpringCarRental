package com.neosoft.vehicleManagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neosoft.vehicleManagement.models.Car;
import com.neosoft.vehicleManagement.serviceImp.CarsServiceImp;

@Controller
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CarsServiceImp carsServ;

	@GetMapping
	public String getHomePage(Model model) {
		addCustomerRole(model);
		return "home";
	}
	
	@GetMapping("/available-cars")
	public String getAvailableCars(Model model) {
		
//		List<Car> cars = carsServ.findFreeCars();
//		
//		addCustomerRole(model);
//		model.addAttribute("cars", cars);
//		
//		return "available-cars";
		
		return findPaginatedAvailableCars(1, "id", "asc", model);
	}
	
	@GetMapping("/available-cars/{pageNo}")
	public String findPaginatedAvailableCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsServ.findPaginatedFreeCars(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addCustomerRole(model);
		model.addAttribute("cars", cars);
		
		return "available-cars";
	}


	@GetMapping("/rented-cars")
	public String getRentedCars(Model model) {
		
//		List<Car> cars = carsServ.findRentedCarsByMail(getUserName());
//		
//		addCustomerRole(model);
//		model.addAttribute("cars", cars);
//		
//		return "rented-cars";
		return findPaginatedRentedCars(1, "id", "asc", model);
	}
	
	@GetMapping("/rented-cars/{pageNo}")
	public String findPaginatedRentedCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsServ.findRentedCarsByMail(pageNo, pageSize, sortField, sortDir, getUserName());
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addCustomerRole(model);
		model.addAttribute("cars", cars);
		
		return "rented-cars";
	}
	
	@PostMapping("/rent-car/{carId}")
	public String postRentCar(@PathVariable("carId") Long id) {
		
		String custMail = getUserName();
		
		if (carsServ.findIfCarBooked(id) == 'Y') {
			return "redirect:/customer/rented-cars?already-booked";
		}
		
		carsServ.bookCarByEmail(id, custMail);
		
		return "redirect:/customer/rented-cars";
	}
	
	@PostMapping("/return-car/{carId}")
	public String postReturnCar(@PathVariable("carId") Long id) {
		
		carsServ.returnCar(id);
		
		return "redirect:/customer/rented-cars";
	}
	
	private void addCustomerRole(Model model) {
		model.addAttribute("authority", "ROLE_CUSTOMER");
	}
	
	private String getUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		return auth.getName();
	}
	
	private void addPaginationAttributes(
			Model model, 
			int pageNo,
			Page<Car> page,
			String sortField,
			String sortDir
	) {
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
	}
	
}
