package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Tag(
        name = "it was repository to the Patient database it use to save the data in mysql databse"
)
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Patient findByPatientEmail(String findByPatientEmail);
    List<Patient> findAllByPatientIdIn(List<Long> patientIds);




}
