package com.neosoft.vehicleManagement.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.neosoft.vehicleManagement.exception.ResourceNotFoundException;
import com.neosoft.vehicleManagement.models.Car;
import com.neosoft.vehicleManagement.repo.CarsRepo;
import com.neosoft.vehicleManagement.services.CarsService;

@Service
public class CarsServiceImp implements CarsService {

	@Autowired
	CarsRepo carsRepo;

	@Override
	public Car save(Car car) {
		return carsRepo.save(car);
	}

	@Override
	public char findIfCarBooked(Long id) {
		return carsRepo.findIfCarBooked(id);
	}

	@Override
	public List<Car> getAllCars() {
		return carsRepo.findAll();
	}

	@Override
	public Car findById(Long id) {
		Car car = carsRepo.findById(id).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", id));
		return car;
	}

	@Override
	public Car updateCar(Car car) {
		Car existingCar = carsRepo.findById(car.getId()).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", car.getId()));
		
		existingCar.setBooked(car.getBooked());
		existingCar.setBookedBy(car.getBookedBy());
		existingCar.setFuelType(car.getFuelType());
		existingCar.setMileage(car.getMileage());
		existingCar.setModel(car.getModel());
		existingCar.setType(car.getType());
		
		return carsRepo.save(existingCar);
	}

	@Override
	public void deleteCar(Long id) {
		carsRepo.findById(id).orElseThrow(() -> 
							new ResourceNotFoundException("Car", "CarID", id));
		
		carsRepo.deleteById(id);
	}

	@Override
	public void bookCarByEmail(Long carId, String custMail) {
		
		Car car = carsRepo.findById(carId).orElseThrow(
					() -> new ResourceNotFoundException("Car", "CarID", carId)
		);
		
		car.setBooked('Y');
		car.setBookedBy(custMail);
		
		carsRepo.save(car);
		
	}

//	@Override
//	public List<Car> findRentedCarsByMail(String mail) {
//
//		List<Car> cars = carsRepo.findByBookedBy(mail);
//		
//		return cars;
//	}

	@Override
	public void returnCar(Long id) {
		Car car = carsRepo.findById(id).orElseThrow(
					() -> new ResourceNotFoundException("Car", "CarID", id)
				);
		
		car.setBooked('N');
		car.setBookedBy(null);
		
		carsRepo.save(car);
	}

	@Override
	public Page<Car> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findAll(pageable);
	}

	@Override
	public Page<Car> findPaginatedFreeCars(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findFreeCarsPaginated(pageable);
	}
	
	@Override
	public Page<Car> findRentedCarsByMail(
			int pageNo, int pageSize, String sortField, String sortDirection,
			String mail
	) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return carsRepo.findByBookedBy(mail, pageable);
	}
	
}
