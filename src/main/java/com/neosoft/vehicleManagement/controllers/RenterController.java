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

import com.neosoft.vehicleManagement.dto.CarDto;
import com.neosoft.vehicleManagement.models.Car;
import com.neosoft.vehicleManagement.serviceImp.CarsServiceImp;

@Controller
@RequestMapping("/renter")
public class RenterController {
	
	private final String MANAGE_CARS = "/renter/manage-cars";
	
	@Autowired
	private CarsServiceImp carsService;
	
	@ModelAttribute("car")
    public CarDto carAdditionDto() {
        return new CarDto();
    }

	@GetMapping
	public String getHomePage(Model model) {
		model.addAttribute("authority", "ROLE_RENTER");
		return "home";
	}
	
	@GetMapping("/manage-cars")
	public String getManageCars(Model model) {
		
//		List<Car> cars = carsService.getAllCars();
//		
//		addRenterRole(model);
//		model.addAttribute("cars", cars);
//		
//		return "manage-cars";
		return findPaginatedManageCars(1, "id", "asc", model);
	}
	
	@GetMapping("/manage-cars/{pageNo}")
	public String findPaginatedManageCars(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		
		
		int pageSize = 3;
		Page<Car> page = carsService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Car> cars = page.getContent();
		
		addPaginationAttributes(model, pageNo, page, sortField, sortDir);
		addRenterRole(model);
		model.addAttribute("cars", cars);
		
		return "manage-cars";
	}
	
	@GetMapping("/add-car")
	public String getAddCar(Model model) {
		addRenterRole(model);
		
		return "add-car";
	}
	
	@GetMapping("/edit-car/{carID}")
	public String getEditCar(
			@PathVariable("carID") Long id,
			Model model
	) {
		Car prevCar = carsService.findById(id);
		
		addRenterRole(model);
		model.addAttribute("carObj", prevCar);
		
		return "edit-car";
	}
	
	@PostMapping("/add-car")
	public String addNewCar(
			@ModelAttribute("car") CarDto carDto
	) {
		Car car = new Car();
		
		car.setModel(carDto.getModel());
		car.setType(carDto.getType());
		car.setFuelType(carDto.getFuelType());
		car.setMileage(carDto.getMileage());
		
		carsService.save(car);
		
		return "redirect:" + MANAGE_CARS;
	}
	
	@PostMapping("/save-edit-car/{carID}")
	public String postAddCar(
			@ModelAttribute("car") CarDto carDto,
			@PathVariable("carID") Long id,
			Model model
	) {
		
		Car car = new Car();
		
		car.setId(id);
		car.setModel(carDto.getModel());
		car.setType(carDto.getType());
		car.setFuelType(carDto.getFuelType());
		car.setMileage(carDto.getMileage());
		
		carsService.updateCar(car);
		
		return "redirect:" + MANAGE_CARS;
	}
	
	@PostMapping("/delete-car/{carID}")
	public String deleteCarById(@PathVariable("carID") Long id) {
		carsService.deleteCar(id);
		
		return "redirect:" + MANAGE_CARS;
	}
	
	private void addRenterRole(Model model) {
		model.addAttribute("authority", "ROLE_RENTER");
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
