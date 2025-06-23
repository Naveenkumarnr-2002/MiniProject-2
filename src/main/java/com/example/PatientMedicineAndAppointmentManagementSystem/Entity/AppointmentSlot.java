package com.example.PatientMedicineAndAppointmentManagementSystem.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String time;

    private  String slot;

    private boolean booked;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
}