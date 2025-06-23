package com.example.PatientMedicineAndAppointmentManagementSystem.Dto;


import lombok.Data;

@Data
public class AdminDto {

    private Long id;
    private String name;
    private String emailId;
    private String password;
    private Long phoneNumber;

}
