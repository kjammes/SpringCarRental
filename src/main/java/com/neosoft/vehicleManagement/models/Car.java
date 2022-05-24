package com.neosoft.vehicleManagement.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	double mileage;
	String model, fuelType, type;
	/* 
	 * N - N means not booked
	 * Y - Y means booked 
	 *  
	 *  */
	char booked = 'N';
	
	@Column(name = "booked_by")
	String bookedBy;

}
