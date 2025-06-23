package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentServiceHistory;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.DoctorService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PatientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PatientService patientService;
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private DoctorService doctorService;
    @Mock
    private AppointmentServiceHistory appointmentServiceHistory;

    @InjectMocks
    private PatientController patientController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    void dashboard_shouldAddPatientAndAppointmentsToModel() throws Exception {
        PatientDto patientDto = new PatientDto();
        List<AppointmentDTO> appointments = List.of(new AppointmentDTO());
        when(authentication.getName()).thenReturn("test@example.com");
        when(patientService.findByEmail("test@example.com")).thenReturn(patientDto);
        when(appointmentService.getAppointmentsByPatientEmail("test@example.com")).thenReturn(appointments);

        mockMvc.perform(get("/patient/dashboard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("patient_dashboard"))
                .andExpect(model().attribute("patient", patientDto))
                .andExpect(model().attribute("allappointments", appointments));
    }

    @Test
    void makeAppointmentPage_shouldAddPatientAndDoctorsToModel() throws Exception {
        PatientDto patientDto = new PatientDto();
        List<DoctorDto> doctors = List.of(new DoctorDto());
        when(authentication.getName()).thenReturn("test@example.com");
        when(patientService.findByEmail("test@example.com")).thenReturn(patientDto);
        when(doctorService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/patient/make_appointment_page").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("make_Appointment"))
                .andExpect(model().attribute("patient", patientDto))
                .andExpect(model().attribute("alldoctors", doctors));
    }


    @Test
    void saveAppointment_shouldSaveAndRedirect() throws Exception {
        PatientDto patientDto = new PatientDto();
        patientDto.setPatientId(1L);
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        when(authentication.getName()).thenReturn("test@example.com");
        when(patientService.findByEmail("test@example.com")).thenReturn(patientDto);

        mockMvc.perform(post("/patient/save_Appointment")
                        .flashAttr("book_appointment", appointmentDTO)
                        .principal(authentication))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/dashboard?success"));

        verify(appointmentService).saveAppointment(any(AppointmentDTO.class));
        verify(appointmentServiceHistory).saveAppointment(any(AppointmentDTO.class));
    }

    @Test
    void deleteAppointment_shouldDeleteAndRedirect() throws Exception {
        mockMvc.perform(get("/patient/deleteAppointement/7"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patient/dashboard?delete"));
        verify(appointmentService).deleteAppointment(7L);
    }
}
