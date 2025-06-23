package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDto doctorDto;


    @BeforeEach
    public void setUp()
    {
        doctorDto=new DoctorDto();
        doctorDto.setDoctorName("thrivendrs");
        doctorDto.setSpecialization("heart");
        doctorDto.setDoctorEmail("thrivendra123@gmail.com");
        doctorDto.setDoctorPassword("hearts");
        doctorDto.setDoctorContact(1234567890L);

        doctor =new Doctor();
        doctor.setDoctorName("thrivendrs");
        doctor.setSpecialization("heart");
        doctor.setDoctorEmail("thrivendra123@gmail.com");
        doctor.setDoctorPassword("hearts");
        doctor.setDoctorContact(1234567890L);
    }

    @Test
    public void givenDOctorData_whensave_thenreturnPatiernDto()
    {
        when(modelMapper.map(doctorDto,Doctor.class)).thenReturn(doctor);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(modelMapper.map(doctor,DoctorDto.class)).thenReturn(doctorDto);

        DoctorDto doctorDto1=doctorService.saveDoctor(doctorDto);

        assertThat(doctorDto1).isNotNull();
    }


    @Test
    public void givendoctoremail_whenfindByEmail_thenreturnDoctor()
    {
        String email="thrivendra123@gmail.com";


        when(doctorRepository.findByDoctorEmail(email)).thenReturn(doctor);
        when(modelMapper.map(doctor,DoctorDto.class)).thenReturn(doctorDto);


        DoctorDto doctorDto1=doctorService.findByEmail(email);

        assertThat(doctorDto1).isNotNull();
        assertThat(doctorDto1.getDoctorEmail()).isEqualTo(email);
    }

    @Test
    public void givendocotrdetias_whenFindByid_thenReturnDocotDto()
    {
        Long id=1L;
        doctor.setDoctorId(id);
        doctorDto.setDoctorId(id);
        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(modelMapper.map(doctor,DoctorDto.class)).thenReturn(doctorDto);

        DoctorDto doctorDto1=doctorService.findById(id);
        assertThat(doctorDto1).isNotNull();
        assertThat(doctorDto1.getDoctorId()).isEqualTo(id);
    }
}
