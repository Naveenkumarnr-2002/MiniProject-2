package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DoctorService {


    DoctorDto saveDoctor(DoctorDto doctorDto);
    DoctorDto findByEmail(String email);
    DoctorDto findById(Long id);
    List<DoctorDto> getAllDoctors();
    void deleteDoctorById(Long id);
    DoctorDto updateDoctor(Long id, DoctorDto doctorDto);
}
