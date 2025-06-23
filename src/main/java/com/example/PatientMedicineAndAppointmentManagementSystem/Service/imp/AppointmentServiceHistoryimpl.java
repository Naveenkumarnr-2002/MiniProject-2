package com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentHistoryDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Appointment;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.AppointmentHistory;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AppointmentHistoryRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentServiceHistory;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentServiceHistoryimpl implements AppointmentServiceHistory
{
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final ModelMapper modelMapper;
    private final PatientRepository patientRepository;

    @Override
    public AppointmentHistoryDTO saveAppointment(AppointmentDTO appointmentDTO) {
        AppointmentHistory appointmentHistory=modelMapper.map(appointmentDTO,AppointmentHistory.class);
        appointmentHistoryRepository.save(appointmentHistory);
        return modelMapper.map(appointmentHistory,AppointmentHistoryDTO.class);
    }
    @Override
    public List<AppointmentHistoryDTO> getAppointmentsByPatientEmail(String email) {
        Patient patient = patientRepository.findByPatientEmail(email);
        if (patient == null) return List.of();
        return appointmentHistoryRepository.findByPatient_PatientId(patient.getPatientId()).stream()
                .map(a -> {
                    AppointmentHistoryDTO dto = modelMapper.map(a, AppointmentHistoryDTO.class);
                    dto.setPatientFirstName(patient.getPatientFirstName() + " " + patient.getPatientLastName());
                    if (a.getDoctor() != null) dto.setDoctorName(a.getDoctor().getDoctorName());
                    return dto;
                })
                .toList();
    }


}
