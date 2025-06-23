package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Appointment;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AppointmentRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceImplTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment appointment;
    private AppointmentDTO appointmentDTO;
    private Patient patient;

    private Doctor doctor;




    @BeforeEach
    void setUp() {
        // Prepare Patient
        patient = new Patient();
        patient.setPatientId(1L);
        patient.setPatientFirstName("Rudrapati THrivendra");
        patient.setPatientLastName("Naidu");
        patient.setPatientEmail("thrivendra123@gmail.com");
        patient.setPatientPassword("encodedPassword");
        patient.setPatientContact(9182280832L);
        patient.setMedicalHistory("Nothing");

        // Prepare Doctor
        doctor = new Doctor();
        doctor.setDoctorId(2L);
        doctor.setDoctorName("thrivendrs");
        doctor.setSpecialization("heart");
        doctor.setDoctorEmail("thrivendra123@gmail.com");
        doctor.setDoctorPassword("hearts");
        doctor.setDoctorContact(1234567890L);

        // Prepare DTO
        appointmentDTO = new AppointmentDTO();
        appointmentDTO.setPatientId(1L);
        appointmentDTO.setDoctorId(2L);
        appointmentDTO.setAppointmentDateTime(LocalDateTime.now().plusDays(1));


        appointment=new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
    }


    @Test
    public void givenAppointmentDTO_whenSaveAppointment_thenReturnSavedAppointment()
    {
        when(modelMapper.map(appointmentDTO,Appointment.class)).thenReturn(appointment);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);

        when(modelMapper.map(appointment,AppointmentDTO.class)).thenReturn(appointmentDTO);

        AppointmentDTO appointmentDTO1=appointmentService.saveAppointment(appointmentDTO);

        assertThat(appointmentDTO1).isNotNull();
    }

    @Test
    public void givenAppointmentDTOWIthId_whenGetApptiomentById_thenReturnAppointemertDto() {
        Long id = 1L;
        appointment.setAppointmentId(id);
        appointmentDTO.setAppointmentId(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));
        when(modelMapper.map(appointment, AppointmentDTO.class)).thenReturn(appointmentDTO);

        AppointmentDTO result = appointmentService.getapptiomentbyId(id);

        assertThat(result).isNotNull();
    }



}
