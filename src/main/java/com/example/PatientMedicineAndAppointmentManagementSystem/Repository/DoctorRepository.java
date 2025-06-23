package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Tag(
        name = "it was repository to the doctor database it use to save the data in mysql"
)
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Doctor findByDoctorEmail(String email);

    List<Doctor> findByAdminId(Long id);

}
