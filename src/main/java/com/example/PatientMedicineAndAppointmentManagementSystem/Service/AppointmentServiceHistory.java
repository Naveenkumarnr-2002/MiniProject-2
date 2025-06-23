package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentHistoryDTO;

import java.util.List;

public interface AppointmentServiceHistory {
    AppointmentHistoryDTO saveAppointment(AppointmentDTO appointmentDTO);
    List<AppointmentHistoryDTO> getAppointmentsByPatientEmail(String email);

}
