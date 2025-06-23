package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlotDto {
    private Long id;
    private LocalDate date;
    private String time;
    private boolean booked;
    private Long doctorId;

}
