package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp.PatientServiceImpl;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private PatientDto patientDto;
    private Patient patient;

    @BeforeEach
    void setUp() {
        // Prepare DTO
        patientDto = new PatientDto();
        patientDto.setPatientFirstName("Rudrapati THrivendra");
        patientDto.setPatientLastName("Naidu");
        patientDto.setPatientEmail("thrivendra123@gmail.com");
        patientDto.setPatientPassword("encodedPassword");
        patientDto.setPatientContact(9182280832L);
        patientDto.setMedicalHistory("Nothing");

        // Prepare Entity
        patient = new Patient();
        patient.setPatientFirstName("Rudrapati THrivendra");
        patient.setPatientLastName("Naidu");
        patient.setPatientEmail("thrivendra123@gmail.com");
        patient.setPatientPassword("encodedPassword");
        patient.setPatientContact(9182280832L);
        patient.setMedicalHistory("Nothing");


    }

    @Test
   public void givenPatientDetails_whenSave_thenReturnPatientDto() {
       //given
        when(modelMapper.map(patientDto, Patient.class)).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // Act(when)
        PatientDto result = patientService.savePatient(patientDto);

        // Assert(Then)
        assertThat(result).isNotNull();
        assertThat(result.getPatientFirstName()).isEqualTo("Rudrapati THrivendra");
        assertThat(result.getPatientEmail()).isEqualTo("thrivendra123@gmail.com");
        assertThat(result.getPatientId()).isEqualTo(patientDto.getPatientId());
    }

    @Test
    public void givenUpdatePatientDetails_whenUpdateOldUser_thenReturnPatient() {
        // given
        patientDto.setPatientId(1L); // Set an ID for the DTO
        patient.setPatientId(1L);    // Set the same ID for the entity

        // Mock findById to return the patient
        when(patientRepository.findById(patientDto.getPatientId())).thenReturn(Optional.of(patient));
        // Mock save and mapping
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(patientDto);

        // when
        PatientDto result = patientService.updateolduser(patientDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPatientId()).isEqualTo(patient.getPatientId());
        assertThat(result.getPatientFirstName()).isEqualTo("Rudrapati THrivendra");
    }


    @Test
    public void givenPatientemail_whenfindByPatientEmail_thenreturnPatiern()
    {
        String email="thrivendra123@gmail.com";

        when(patientRepository.findByPatientEmail(email)).thenReturn(patient);
        when(modelMapper.map(patient,PatientDto.class)).thenReturn(patientDto);



        PatientDto savedpatiern=patientService.findByEmail(email);

        assertThat(savedpatiern).isNotNull();
        assertThat(savedpatiern.getPatientId()).isEqualTo(patientDto.getPatientId());
        assertThat(savedpatiern.getPatientEmail()).isEqualTo(email);
    }


    @Test
    public void givenPatient_whenFinalAllpatietens_thenreturnPatienr()
    {
        PatientDto patientDto1=new PatientDto();
        patientDto1.setPatientFirstName("Rudrapati THrivendra");
        patientDto1.setPatientLastName("Naidu");
        patientDto1.setPatientEmail("thrivendra123@gmail.com");
        patientDto1.setPatientPassword("encodedPassword");
        patientDto1.setPatientContact(9182280832L);
        patientDto1.setMedicalHistory("Nothing");

        // Prepare Entity
        Patient patient1 = new Patient();
        patient1.setPatientFirstName("Rudrapati THrivendra");
        patient1.setPatientLastName("Naidu");
        patient1.setPatientEmail("thrivendra123@gmail.com");
        patient1.setPatientPassword("encodedPassword");
        patient1.setPatientContact(9182280832L);
        patient1.setMedicalHistory("Nothing");


        when(patientRepository.findAll()).thenReturn(Arrays.asList(patient,patient1));

        when(modelMapper.map(patient,PatientDto.class)).thenReturn(patientDto);
        when(modelMapper.map(patient1,PatientDto.class)).thenReturn(patientDto1);

        List<PatientDto> allpatient=patientService.getAllPatients();

        assertThat(allpatient).isNotNull();
    }


}
