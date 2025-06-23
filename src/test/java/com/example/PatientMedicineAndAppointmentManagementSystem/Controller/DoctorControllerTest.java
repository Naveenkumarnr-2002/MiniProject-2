package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.DoctorService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DoctorControllerTest {

    private MockMvc mockMvc;

    @Mock private DoctorService doctorService;
    @Mock private AppointmentService appointmentService;
    @Mock private PatientService patientService;
    @Mock private ModelMapper modelMapper;

    @InjectMocks
    private DoctorController doctorController;

    @Mock private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(doctorController).build();
    }

    @Test
    void dashboard_shouldAddDoctorAppointmentsAndPatientMapToModel() throws Exception {
        String email = "doc@example.com";
        DoctorDto doctorDto = new DoctorDto();
        List<AppointmentDTO> appointments = Arrays.asList(
                new AppointmentDTO() {{ setPatientId(1L); }},
                new AppointmentDTO() {{ setPatientId(2L); }}
        );
        List<PatientDto> patients = Arrays.asList(
                new PatientDto() {{ setPatientId(1L); }},
                new PatientDto() {{ setPatientId(2L); }}
        );

        when(authentication.getName()).thenReturn(email);
        when(doctorService.findByEmail(email)).thenReturn(doctorDto);
        when(appointmentService.getAppointmentsByDoctorEmail(email)).thenReturn(appointments);
        when(patientService.findAllByIds(any())).thenReturn(patients);

        mockMvc.perform(get("/doctor/dashboard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor_dashboard"))
                .andExpect(model().attribute("doctor", doctorDto))
                .andExpect(model().attribute("allappointments", appointments))
                .andExpect(model().attributeExists("patientMap"));
    }

    @Test
    void canceltheappoinment_shouldDeleteAndRedirect() throws Exception {
        mockMvc.perform(get("/doctor/canceltheappoinment/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor/dashboard"));
        verify(appointmentService).deleteAppointment(5L);
    }

    @Test
    void acceptpatienr_withExistingPatient_returnsCheckinArea() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId(11L);

        when(patientService.findById(11L)).thenReturn(patientDto);

        mockMvc.perform(get("/doctor/accpetdPatient/21/11"))
                .andExpect(status().isOk())
                .andExpect(view().name("docotr_patient_checkinarea"))
                .andExpect(model().attribute("appointmentpatient", patientDto));
    }


    @Test
    void savethepatientmedicaldetils_shouldUpdatePatientAndDeleteAppointment() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setAppointmentId(100L);
        when(modelMapper.map(patientDto, com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient.class))
                .thenReturn(new com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient());

        mockMvc.perform(post("/doctor/savethepatientmedicaldetils")
                        .flashAttr("appointmentpatient", patientDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor/dashboard"));

        verify(patientService).updateolduser(patientDto);
        verify(appointmentService).deleteAppointment(100L);
    }
}
