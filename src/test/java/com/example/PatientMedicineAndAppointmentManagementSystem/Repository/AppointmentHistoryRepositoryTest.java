package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;




import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Appointment;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AppointmentHistoryRepositoryTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Appointment appointment;

    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    public void setUP() {
        patient = new Patient();
        patient.setPatientFirstName("Rudrapati THrivendra");
        patient.setPatientLastName("Naidu");
        patient.setPatientEmail("thrivendra123@gmail.com");
        patient.setPatientPassword(passwordEncoder.encode("Naidu"));
        patient.setPatientContact(9182280832L);
        patient.setMedicalHistory("Nothing");

        doctor = new Doctor();
        doctor.setDoctorName("thrivendra");
        doctor.setSpecialization("heart");
        doctor.setDoctorEmail("thrivendra123@gmail.com");
        doctor.setDoctorPassword(passwordEncoder.encode("hearts"));
        doctor.setDoctorContact(1234567890L);


        appointment = new Appointment();
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);


    }

    @Test
    public void givenAppointments_whenSave_thenReturnAppointment() {
        // given  (Take data from setup() method )

        //when
        Appointment Savedappointment = appointmentRepository.save(appointment);

        //then
        assertThat(Savedappointment).isNotNull();

    }
}