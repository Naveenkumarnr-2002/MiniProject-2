package com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Appointment;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AppointmentHistoryRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AppointmentRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(
        name = "it make the data to sent to repository"
)
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ModelMapper modelMapper;
    private final AppointmentHistoryRepository appointmentHistoryRepository;

//    @Override
//    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDto) {
//        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
//        // Set patient and doctor references if needed
//        if (appointmentDto.getPatientId() != null) {
//            appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).orElse(null));
//        }
//        if (appointmentDto.getDoctorId() != null) {
//            appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).orElse(null));
//        }
//
//        Appointment saved = appointmentRepository.save(appointment);
//        return modelMapper.map(saved, AppointmentDTO.class);
//    }
//@Override
//public AppointmentDTO saveAppointment(AppointmentDTO appointmentDto) {
//    Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);
//
//    // Set Patient reference
//    if (appointmentDto.getPatientId() != null) {
//        appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).orElse(null));
//    }
//
//    // Set Doctor reference
//    if (appointmentDto.getDoctorId() != null) {
//        appointment.setDoctor(doctorRepository.findById(appointmentDto.getDoctorId()).orElse(null));
//    }
//
//    // appointmentDateTime is mapped automatically by ModelMapper if names match
//
//    Appointment saved = appointmentRepository.save(appointment);
//    return modelMapper.map(saved, AppointmentDTO.class);
//}


    @Operation(
            summary = "it take data and store in database"
    )
    @Override
    public AppointmentDTO saveAppointment(AppointmentDTO appointmentDto) {
        Appointment appointment = modelMapper.map(appointmentDto, Appointment.class);

        // Set Patient reference
        if (appointmentDto.getPatientId() != null) {
            appointment.setPatient(patientRepository.findById(appointmentDto.getPatientId()).orElse(null));
        }

        // Set Doctor reference (THIS IS CRUCIAL)
        if (appointmentDto.getDoctorId() != null) {
            Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId()).orElse(null);
            appointment.setDoctor(doctor);
        }

        Appointment saved = appointmentRepository.save(appointment);
//        AppointmentHistory appointmentHistory=modelMapper.map(appointment, AppointmentHistory.class);
//        appointmentHistoryRepository.save(appointmentHistory);
        return modelMapper.map(saved, AppointmentDTO.class);
    }




    @Operation(
            summary = "it take Patient email id and get full appointment in that email id"
    )
    @Override
    public List<AppointmentDTO> getAppointmentsByPatientEmail(String email) {
        Patient patient = patientRepository.findByPatientEmail(email);
        if (patient == null) return List.of();
        return appointmentRepository.findByPatient_PatientId(patient.getPatientId()).stream()
                .map(a -> {
                    AppointmentDTO dto = modelMapper.map(a, AppointmentDTO.class);
                    dto.setPatientFirstName(patient.getPatientFirstName() + " " + patient.getPatientLastName());
                    if (a.getDoctor() != null) dto.setDoctorName(a.getDoctor().getDoctorName());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "it take DoctorEmail id and get full appointment in that email id"
    )
    @Override
    public List<AppointmentDTO> getAppointmentsByDoctorEmail(String email) {
        Doctor doctor = doctorRepository.findByDoctorEmail(email);
        if (doctor == null)
        {
            return List.of();
        }
        return appointmentRepository.findByDoctor_DoctorId(doctor.getDoctorId())
                .stream().map(appointment -> {
                    AppointmentDTO appointmentDTO = modelMapper.map(appointment, AppointmentDTO.class);
                    appointmentDTO.setDoctorName(doctor.getDoctorName());
                    if (appointment.getPatient() != null)
                    {
                        appointmentDTO.setPatientFirstName(appointment.getPatient().getPatientFirstName() + " " + appointment.getPatient().getPatientLastName());

                    }
                    return appointmentDTO;
                })
                .collect(Collectors.toList());
    }
    @Override
    public List<AppointmentDTO> getAppointmentsByDoctorEmails(String email) {
        Doctor doctor = doctorRepository.findByDoctorEmail(email);
        if (doctor == null) return List.of();
        return appointmentRepository.findByDoctor_DoctorId(doctor.getDoctorId()).stream()
                .map(a -> {
                    AppointmentDTO dto = modelMapper.map(a, AppointmentDTO.class);
                    dto.setDoctorName(doctor.getDoctorName());
                    if (a.getPatient() != null) {
                        dto.setPatientFirstName(a.getPatient().getPatientFirstName() + " " + a.getPatient().getPatientLastName());
                        dto.setPatientId(a.getPatient().getPatientId());
                        // Fetch patient info as needed (not stored in DB)
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

//    @Override
//    public AppointmentDTO getapptiomentbyId(Long id) {
//        Optional<Appointment> appointment=appointmentRepository.findById(id);
//        return modelMapper.map(appointment,AppointmentDTO.class);
//    }

    @Operation(
            summary = "it take appointment id and get full details of thr appointment"
    )
        @Override
        public AppointmentDTO getapptiomentbyId(Long id) {
            Optional<Appointment> appointment = appointmentRepository.findById(id);
            return appointment.map(a -> modelMapper.map(a, AppointmentDTO.class)).orElse(null);
        }


        @Operation(
                summary = "it give all appointments"
        )
    @Override
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(a -> modelMapper.map(a, AppointmentDTO.class))
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "it deleted the Appointment by using Appointment id"
    )
    @Override
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

//    @Override
//    public List<AppointmentDTO> getAppointmentsByDoctorId(Long id)
//    {
//        Optional<Doctor> doctor=doctorRepository.findById(id);
//        return AppointmentDTO
//    }
}
