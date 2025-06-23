package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.MedicationDTO;

import java.util.List;

public interface MedicationService {
    void saveMedication(MedicationDTO dto);
    List<MedicationDTO> getMedicationsByPatient( Long patientId);
}
