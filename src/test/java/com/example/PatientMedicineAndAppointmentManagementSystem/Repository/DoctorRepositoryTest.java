package com.example.PatientMedicineAndAppointmentManagementSystem.Repository;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class DoctorRepositoryTest
{
    @TestConfiguration

    static class Testconfog
    {
        @Bean
        public PasswordEncoder passwordEncoder()
        {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private Doctor doctor;


    @BeforeEach
    public void setUp()
    {
        doctor=new Doctor();
        doctor.setDoctorName("thrivendra");
        doctor.setSpecialization("heart");
        doctor.setDoctorEmail("thrivendra123@gmail.com");
        doctor.setDoctorPassword(passwordEncoder.encode("hearts"));
        doctor.setDoctorContact(1234567890L);
    }

    @Test
    public void givenDoctor_whenSAve_thenReturnsaveddoctor()
    {
        //given take ths data from setup()

        //when
        Doctor doctor1=doctorRepository.save(doctor);

        //then
        assertThat(doctor1).isNotNull();
    }

    @Test
    public void givenDoctor_whenfindByDoctorEmail_thenReturnDoctor()
    {
        // given
        Doctor doctor1=doctorRepository.save(doctor);
        String email="thrivendra123@gmail.com";

        //when
        Doctor doctor2=doctorRepository.findByDoctorEmail(email);

        //then
        assertThat(doctor2).isNotNull();
        assertThat(doctor2.getDoctorEmail()).isEqualTo(email);

    }

    @Test
    public void givenDoctor_whenfindById_thenreturnDoctor()
    {
        //given
        Doctor doctor1=doctorRepository.save(doctor);
        Long id=1L;

        //when
        Optional<Doctor> existingDoctor=doctorRepository.findById(id);

        //then
        assertThat(existingDoctor).isNotEmpty();
        assertThat(existingDoctor.get().getDoctorId()).isEqualTo(id);
        assertThat(existingDoctor.get().getDoctorEmail()).isEqualTo(doctor1.getDoctorEmail());
    }

    @Test
    public void givenDoctors_whenFindAll_thenReturnAllDoctors()
    {
        //given
        Doctor saveddocotr1=doctorRepository.save(doctor);

        Doctor doctor1=new Doctor();
        doctor1.setDoctorName("thrivendra");
        doctor1.setSpecialization("heart");
        doctor1.setDoctorEmail("thrivendranaidu@gmail.com");
        doctor1.setDoctorPassword(passwordEncoder.encode("hearts"));
        doctor1.setDoctorContact(1234567890L);

        Doctor saveddocotr2=doctorRepository.save(doctor1);

        //when
        List<Doctor> alldoctors=doctorRepository.findAll();

        //then

        assertThat(alldoctors).isNotEmpty();
        assertThat(alldoctors.size()).isEqualTo(2);
    }
}
