package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PatientRepositoryTest {
    @TestConfiguration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

   @Autowired
   private PatientRepository patientRepository;
   @Autowired
   private PasswordEncoder passwordEncoder;

   private Patient patient;


    @BeforeEach
    public void setUp()
    {
        patient=new Patient();
        patient.setPatientFirstName("Rudrapati THrivendra");
        patient.setPatientLastName("Naidu");
        patient.setPatientEmail("thrivendra123@gmail.com");
        patient.setPatientPassword(passwordEncoder.encode("Naidu"));
        patient.setPatientContact(9182280832L);
        patient.setMedicalHistory("Nothing");
    }

    @Test
    public void givenPatient_whenSave_thenReturnPatient()
    {
        //given(Take the data for the setup() method

        //when
        Patient savedpatient=patientRepository.save(patient);

        //then return
        assertThat(savedpatient).isNotNull();
//        assertThat(savedpatient.getPatientId()).isGreaterThan(0);
//        assertThat(savedpatient.getPatientFirstName()).isEqualTo("Rudrapati THrivendra");

    }

    @Test
    public void gievenPatient_whenfindByEmail_thenreturn_nullforEMpty_error()
    {
        //given
        Patient saveddata=patientRepository.save(patient);

        //when
        Patient olduser=patientRepository.findByPatientEmail(saveddata.getPatientEmail());

        //then
        assertThat(olduser).isNotNull();
        assertThat(saveddata.getPatientEmail()).isEqualTo(olduser.getPatientEmail());

    }

    @Test
    public void givenpatient_whenFIndbyPatientID_thenReturnPatinent()
    {
        //given

        Patient patient1=patientRepository.save(patient);

        // when
        Optional<Patient> existingpatient=patientRepository.findById(patient1.getPatientId());

        // then
        assertThat(existingpatient).isNotEmpty();
        assertThat(existingpatient.get().getPatientId()).isEqualTo(patient1.getPatientId());
    }
}
