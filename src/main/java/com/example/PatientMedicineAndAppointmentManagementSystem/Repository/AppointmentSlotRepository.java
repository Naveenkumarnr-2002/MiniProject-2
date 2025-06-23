package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.AppointmentSlot;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {

    List<AppointmentSlot> findByDoctorDoctorId(Long doctorId);

    List<AppointmentSlot> findByDoctorAndBooked(Doctor doctor, boolean booked);

    Optional<AppointmentSlot> findByDoctorAndDateAndTime(Doctor doctor, LocalDate date, String time);

}
