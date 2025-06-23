package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import java.util.List;

public interface PatientService {
    PatientDto savePatient(PatientDto patientDto);
    PatientDto findByEmail(String email);
    PatientDto findById(Long id);
    List<PatientDto> getAllPatients();
    List<PatientDto> findAllByIds(Iterable<Long> ids);
    PatientDto updateolduser(PatientDto patientDto);



}
