package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.time.LocalDateTime;

@Tag(
        name = "It contains the details of the doctor, patient, and appointment."

)
@Data
public class AppointmentDTO {
    private Long appointmentId;
    private LocalDateTime appointmentDateTime;
    private Long patientId;
    private String patientFirstName;
    private String patientLastName;
    private String patientEmail;
    private Long patientContact;
    private Boolean status;

    private String medicalHistory;

    private Long doctorId;
    private String doctorName;

}

