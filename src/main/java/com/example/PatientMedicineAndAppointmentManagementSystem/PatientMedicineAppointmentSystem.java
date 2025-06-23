package com.example.PatientMedicineAndAppointmentManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PatientMedicineAppointmentSystem {


	public static void main(String[] args) {
		SpringApplication.run(PatientMedicineAppointmentSystem.class, args);
		System.out.println(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("admin"));

	}

}
