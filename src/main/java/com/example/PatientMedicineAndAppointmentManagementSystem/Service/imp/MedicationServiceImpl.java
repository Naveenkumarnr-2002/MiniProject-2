package com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.MedicationDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Medication;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.MedicationRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public void saveMedication(MedicationDTO dto) {
        Doctor doctor = doctorRepository.findById(dto.getDoctorId()).orElseThrow();
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();

        Medication medication = Medication.builder()
                .prescribedDate(dto.getPrescribedDate())
                .doctor(doctor)
                .patient(patient)
                .build();

        medicationRepository.save(medication);
    }

    @Override
    public List<MedicationDTO> getMedicationsByPatient(Long patientId) {
        List<Medication> meds = medicationRepository.findByPatientPatientId(patientId);
        return meds.stream().map(med -> {
            MedicationDTO dto = new MedicationDTO();
            dto.setMedicationId(med.getMedicationId());
            dto.setPatientId(med.getPatient().getPatientId());
//            dto.setDoctorId(med.getDoctor().getDoctorId());
//            dto.setDoctorName(med.getDoctor().getDoctorName());
//            dto.setPrescribedDate(med.getPrescribedDate());
            dto.setMedicationDetails(med.getMedicationDetails());
            return dto;
        }).collect(Collectors.toList());

//        return medicationRepository.findByPatientPatientId(patientId)
//                .stream()
//                .map(med -> MedicationDTO.builder()
//                        .medicationId(med.getMedicationId())
//                        .prescribedDate(med.getPrescribedDate())
//                        .patientId(patientId)
//                        .build())
//                .collect(Collectors.toList());
    }
}
