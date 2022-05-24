package com.neosoft.vehicleManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CarDto {

	Long id;
	double mileage;
	String model, fuelType, type;
	/* 
	 * N - N means not booked
	 * Y - Y means booked 
	 *  
	 *  */
	char booked = 'N';
	String bookedBy;
	
}
