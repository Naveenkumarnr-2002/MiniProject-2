package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;


import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Data
public class AppointmentHistoryDTO {
    private Long appointmentId;
    private LocalDateTime appointmentDateTime;
    private Long patientId;
    private String patientFirstName;
    private Long doctorId;
    private String doctorName;
    private String status;
    private  String slot;





}

