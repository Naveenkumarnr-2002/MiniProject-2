package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

@Tag(
        name = "it contains doctor full details"
)
@Data
public class DoctorDto {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private String doctorEmail;
    private String doctorPassword;
    private Long doctorContact;
}
