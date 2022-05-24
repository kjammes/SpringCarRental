package com.neosoft.vehicleManagement.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.neosoft.vehicleManagement.dto.UserRUDto;
import com.neosoft.vehicleManagement.services.UserService;

@Controller
@RequestMapping("/registration")
public class UserRegistrationController {

	@Autowired
	private UserService userService;

	
	@ModelAttribute("user")
    public UserRUDto userRegistrationDto() {
        return new UserRUDto();
    }
	
	@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}
	
	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRUDto registrationDto) {
		userService.save(registrationDto);
		return "redirect:/registration?success";
	}
}
