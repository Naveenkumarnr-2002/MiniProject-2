package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicationDTO {
    private Long medicationId;
    private String medicationDetails;
    private LocalDate prescribedDate;
    private Long patientId;
    private Long doctorId;
    private String doctorName;
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private Long patientContact;
    private String medicalHistory;
}
