package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(
        name = "it contains details of patient"
)
@Data
public class PatientDto {
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private String patientPassword;
    private Long patientContact;
    private String medicalHistory;


    private Long appointmentId;

}
