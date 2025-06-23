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
public class AppointmentRepositoryTest {

    @TestConfiguration
    static class TestConfig
    {
        @Bean
        public PasswordEncoder passwordEncoder()
        {
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
    public void setUP()
    {
        patient=new Patient();
        patient.setPatientFirstName("Rudrapati THrivendra");
        patient.setPatientLastName("Naidu");
        patient.setPatientEmail("thrivendra123@gmail.com");
        patient.setPatientPassword(passwordEncoder.encode("Naidu"));
        patient.setPatientContact(9182280832L);
        patient.setMedicalHistory("Nothing");

        doctor=new Doctor();
        doctor.setDoctorName("thrivendra");
        doctor.setSpecialization("heart");
        doctor.setDoctorEmail("thrivendra123@gmail.com");
        doctor.setDoctorPassword(passwordEncoder.encode("hearts"));
        doctor.setDoctorContact(1234567890L);


        appointment=new Appointment();
        appointment.setAppointmentDateTime(LocalDateTime.now().plusDays(1));
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);


    }

    @Test
    public void givenAppointments_whenSave_thenReturnAppointment()
    {
        // given  (Take data from setup() method )

        //when
        Appointment Savedappointment=appointmentRepository.save(appointment);

        //then
        assertThat(Savedappointment).isNotNull();

    }

    @Test
    public void givenDocotrEMail_whengetAppointmentsByDoctorEmail_thenReturnThedoctors()
    {
        //given
        doctorRepository.save(doctor);
        patientRepository.save(patient);
        Appointment savedappointment1=appointmentRepository.save(appointment);

        Appointment appointment1=new Appointment();
        appointment1.setAppointmentDateTime(LocalDateTime.now().plusDays(2));
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        Appointment savedappointment2=appointmentRepository.save(appointment1);

        //when
//        Doctor ExistingDocotr=doctorRepository.findByDoctorEmail("thrivendra123@gmail.com");
        List<Appointment> appointmentList=appointmentRepository.findByDoctor_DoctorId(doctor.getDoctorId());

        //then
        assertThat(appointmentList).isNotNull();
        assertThat(appointmentList.size()).isEqualTo(2);
    }

    @Test
    public void givenAppoinemtes_whenFindById_thenreturnAppointmedt()
    {
        //given
        Long id=1L;
        doctorRepository.save(doctor);
        patientRepository.save(patient);
        Appointment savedappointment1=appointmentRepository.save(appointment);

        //when
        Optional<Appointment> bookedappoinment=appointmentRepository.findById(savedappointment1.getAppointmentId());

        //then

        assertThat(bookedappoinment).isNotNull();
        assertThat(bookedappoinment.get().getDoctor().getDoctorEmail()).isEqualTo(doctor.getDoctorEmail());
    }

    @Test
    public void givenAppoinmnets_whenfindAll_thenReturnALlAppoinments()
    {
        //given
            doctorRepository.save(doctor);
            patientRepository.save(patient);
        Appointment savedappointment1=appointmentRepository.save(appointment);

        Appointment appointment1=new Appointment();
        appointment1.setAppointmentDateTime(LocalDateTime.now().plusDays(2));
        appointment1.setPatient(patient);
        appointment1.setDoctor(doctor);
        Appointment savedappointment2=appointmentRepository.save(appointment1);

        //when

        List<Appointment> appointmentList=appointmentRepository.findAll();

        //then
        assertThat(appointmentList).isNotNull();
        assertThat(appointmentList.size()).isEqualTo(2);
    }

    @Test
    public void givenAppoinment_WhendeleteAppointmentbyid_thenNoreturn()
    {
        //given
        doctorRepository.save(doctor);
        patientRepository.save(patient);
        Appointment savedappointment1=appointmentRepository.save(appointment);
        Long id=savedappointment1.getAppointmentId();

        //when
        appointmentRepository.deleteById(id);

        //then
        Optional<Appointment> existing=appointmentRepository.findById(id);

        assertThat(existing).isEmpty();


    }
}
