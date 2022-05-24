package com.neosoft.vehicleManagement.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.neosoft.vehicleManagement.models.Car;

public interface CarsService {
	Car save(Car car);
	
	void bookCarByEmail(Long carId, String custMail);
	
	char findIfCarBooked(Long id);
	
	Page<Car> findPaginatedFreeCars(int pageNo, int pageSize, String sortField, String sortDirection);
	
//	List<Car> findRentedCarsByMail(String mail);
	
	List<Car> getAllCars();
	
	Page<Car> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	
	Car findById(Long id);
	
	Car updateCar(Car car);
	
	void returnCar(Long id);
	
	void deleteCar(Long id);

	Page<Car> findRentedCarsByMail(int pageNo, int pageSize, String sortField, String sortDirection, String mail);
	
}
