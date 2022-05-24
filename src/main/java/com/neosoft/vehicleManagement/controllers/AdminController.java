package com.neosoft.vehicleManagement.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.neosoft.vehicleManagement.dto.UserRUDto;
import com.neosoft.vehicleManagement.exception.ResourceNotFoundException;
import com.neosoft.vehicleManagement.models.User;
import com.neosoft.vehicleManagement.repo.UserRepo;
import com.neosoft.vehicleManagement.serviceImp.UserServiceImpl;
import com.neosoft.vehicleManagement.util.MyUtility;

@Controller
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	UserServiceImpl userServ;
	
	@ModelAttribute("user")
    public UserRUDto userRegistrationDto() {
        return new UserRUDto();
    }
	
	//Getting home page

	@GetMapping
	public String getHomePage(Model model) {
		addAdminRole(model);
		return "home";
	}
	
	//All the routes handling renters
	
	@GetMapping("/manage-renters")
	public String getManageRenters(Model model) {
		
//		List<User> renters = userRepo.findAll();
//		renters = MyUtility.getUserListByRole("ROLE_RENTER", renters);
//		
//		addAdminRole(model);
//		model.addAttribute("rentersList", renters);
		
//		return "manage-renters";
		return findPaginatedManageRenters(1, "id", "asc", model);
	}

	@GetMapping("/edit-renter/{renterID}")
	public String getEditRenterPage(
			Model model, 
			@PathVariable("renterID") Long id
	) {
		System.out.println("This is EDIT RENTER controller in admin");
		addAdminRole(model);
		
		User renter = userRepo.getById(id);
		model.addAttribute("renterObj", renter);
		
		return "edit-renter";
	}
	
	@PostMapping("/save-edit-renter/{renterID}")
	public String saveEditedRenter(
			@ModelAttribute("user") UserRUDto updationDto,
			@PathVariable("renterID") Long id
	) {
		User existingUser = userRepo.getById(id);
		
		if (existingUser == null)
			throw new ResourceNotFoundException("User", "UserID", id);
		
		existingUser.setEmail(updationDto.getEmail());
		existingUser.setFirstName(updationDto.getFirstName());
		existingUser.setLastName(updationDto.getLastName());
		
		userRepo.save(existingUser);
		
		return "redirect:/admin/manage-renters";
	}
	
	@PostMapping("/remove-renter/{id}")
	public String removeRenter(@PathVariable("id") Long id) {
		
		if (id == null)
			throw new RuntimeException("User ID should NOT be null");
		
		userRepo.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("User", "UserID", id));
		
		userRepo.deleteById(id);
		
		return "redirect:/admin/manage-renters";
	}
	
	// All the routes handling normal customers
	
	@GetMapping("/manage-customers")
	public String getManageCustomers(Model model) {
		
//		List<User> customers = userRepo.findAll();
//		customers = MyUtility.getUserListByRole("ROLE_CUSTOMER", customers);
//		
//		addAdminRole(model);
//		model.addAttribute("customersList", customers);
//		
//		return "manage-customers";
		return findPaginatedManageCustomers(1, "id", "asc", model);
	}
	
	@PostMapping("/remove-customer/{id}")
	public String removeCustomer(@PathVariable("id") Long id) {
		
		if (id == null)
			throw new RuntimeException("User ID should NOT be null");
		
		userRepo.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("User", "UserID", id));
		
		userRepo.deleteById(id);
		
		return "redirect:/admin/manage-customers";
	}
	
	@GetMapping("/manage-renters/{pageNo}")
	public String findPaginatedManageRenters(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		
		Page<User> page = userServ.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> users = page.getContent();
		
		users = MyUtility.getUserListByRole("ROLE_RENTER", users);
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("rentersList", users);
		
		return "manage-renters";
	}
	
	@GetMapping("/manage-customers/{pageNo}")
	public String findPaginatedManageCustomers(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<User> page = userServ.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<User> users = page.getContent();
		users = MyUtility.getUserListByRole("ROLE_CUSTOMER", users);
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addAdminRole(model);
		model.addAttribute("customersList", users);
		
		return "manage-customers";
	}
	
	private void addAdminRole(Model model) {
		model.addAttribute("authority", "ROLE_ADMIN");
	}
	
	private void addPaginationAttributes(
			Model model, 
			int pageNo,
			Page<User> page,
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
