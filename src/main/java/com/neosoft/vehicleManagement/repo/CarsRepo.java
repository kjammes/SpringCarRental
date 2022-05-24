package com.neosoft.vehicleManagement.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import com.neosoft.vehicleManagement.models.Car;

public interface CarsRepo extends JpaRepository<Car, Long> {
	@Query("SELECT c.booked FROM Car c WHERE c.id = ?1")
	char findIfCarBooked(Long id);
	
	@Query("SELECT c FROM Car c WHERE c.booked = 'N'")
	Page<Car> findFreeCarsPaginated(Pageable pageable);
	
	Page<Car> findByBookedBy(String mail, Pageable pageable);
}
