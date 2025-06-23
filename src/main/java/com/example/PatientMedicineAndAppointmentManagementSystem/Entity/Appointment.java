package com.example.PatientMedicineAndAppointmentManagementSystem.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;

    private LocalDateTime appointmentDateTime;
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private Long patientContact;
    private String medicalHistory;

    @OneToOne
    private AppointmentSlot slot;

    private Boolean status;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id") // This must match your DB column
    private Doctor doctor;
}


