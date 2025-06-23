package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.*;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.*;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.*;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Tag(
        name = "Make all doctor opertions or controller in it"
)

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PatientService patientService;
    private final ModelMapper modelMapper;
    private final PatientRepository patientRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicationService medicationService;

    private final DoctorRepository doctorRepository;
    private final MedicationRepository medicationRepository;


    @Operation(
            summary = "it open the dashboard of doctor with appointment "
    )
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {

        String email = authentication.getName();


        DoctorDto doctor = doctorService.findByEmail(email);
        model.addAttribute("doctor", doctor);


        List<AppointmentDTO> appointments = appointmentService.getAllAppointments();
        model.addAttribute("allAppointments", appointments);


        Set<Long> patientIds = appointments.stream()
                .map(AppointmentDTO::getPatientId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());


        List<Long> patientIdList = new ArrayList<>(patientIds);


        List<PatientDto> patients = patientService.findAllByIds(patientIdList);


        Map<Long, PatientDto> patientMap = patients.stream()
                .collect(Collectors.toMap(PatientDto::getPatientId, p -> p));
        model.addAttribute("patientMap", patientMap);

        return "doctor_dashboard";
    }


    @Operation(
            summary = "it delete the appointment if docotrs select cancel"
    )

    @GetMapping("/canceltheappoinment/{id}")
    public String canceltheappoinment(@PathVariable("id") Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/doctor/dashboard";
    }


    @Operation(
            summary = "it accept the appointment for doctor so he can get treatment for doctor"
    )

    @GetMapping("/acceptedPatient/{appointmentId}/{patientId}")
    public String approveAppointment(@PathVariable("appointmentId") Long appointmentId,
                                     @PathVariable("patientId") Long patientId,
                                     Model model, RedirectAttributes redirectAttributes) {
//        PatientDto patientDto = patientService.findById(patientId);
//        if (patientDto == null) {
//            model.addAttribute("error", "Patient not found");
//            return "error_page"; // Use your error page
//        }
//        patientDto.setAppointmentId(appointmentId); // Set appointmentId in DTO
//        model.addAttribute("appointmentpatient", patientDto);
//        return "docotr_patient_checkinarea";

        Appointment appointmentHistory = appointmentRepository.findById(appointmentId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Doctor doctor = new Doctor();
        AppointmentDTO appointment = new AppointmentDTO();
        appointment.setAppointmentId(appointmentId);
        appointment.setPatientFirstName(patient.getPatientFirstName());
        appointment.setPatientLastName(patient.getPatientLastName());
        appointment.setPatientEmail(patient.getPatientEmail());
        appointment.setPatientContact(patient.getPatientContact());
        appointment.setMedicalHistory(patient.getMedicalHistory());

        model.addAttribute("appointmentPatient", appointment);


        AppointmentDTO dto = new AppointmentDTO();
        Appointment appointment1 = appointmentRepository.findById(appointmentId).orElseThrow();
        appointment1.setStatus(true);
        appointmentRepository.save(appointment1);


        AppointmentSlot slot = new AppointmentSlot();
        slot.setBooked(true);
        appointmentSlotRepository.save(slot);
        redirectAttributes.addFlashAttribute("message", "Appointment marked approved and slot reopened.");
        model.addAttribute("doctorId", doctor.getDoctorId());
        model.addAttribute("patientId", patient.getPatientId());

        return "doctor_patient_checkInArea";
    }


    @Operation(
            summary = "after the treatment the doctor give some medical and after save it store the new medical records data in patient database"
    )
    @GetMapping("/medicationsForm/{patientId}")
    public String showMedicationForm(@PathVariable Long patientId,
                                     Model model) {
//        patientService.updateolduser(patientDto);
//        Patient patient=modelMapper.map(patientDto,Patient.class);
//        AppointmentDTO appointmentDTO=appointmentService.getapptiomentbyId(appointmentId);
//        appointmentServiceHistory.saveAppointment(appointmentDTO);
//        appointmentService.deleteAppointment(patientDto.getAppointmentId());
//        return "redirect:/doctor/dashboard";
//        Patient patient=patientRepository.findById(dto.getPatientId()).orElseThrow();
//        patient.setMedicalHistory(dto.getMedicalHistory());
//        patientRepository.save(patient);


//        redirectAttributes.addFlashAttribute("message", "Appointment marked approved and slot reopened.");
        Patient patient = new Patient();
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setPatientId(patientId);
        medicationDTO.setMedicalHistory(patient.getMedicalHistory());
        medicationDTO.setPatientFirstName(patient.getPatientFirstName());
        medicationDTO.setPatientLastName(patient.getPatientLastName());
        medicationDTO.setPrescribedDate(LocalDate.now());
        if (medicationDTO.getPatientId() == null) {
            throw new IllegalArgumentException("Patient ID must not be null");
        }

        model.addAttribute("medication", medicationDTO);
        return "medications";
    }


    @PostMapping("/medications/save")
    public String saveMedication(@ModelAttribute("medication") MedicationDTO dto,
                                 RedirectAttributes redirectAttributes) {
        Patient patient = patientRepository.findById(dto.getPatientId()).orElseThrow();

        Medication medication = new Medication();
        medication.setPatient(patient);
        medication.setMedicationDetails(dto.getMedicationDetails());
        medication.setPrescribedDate(LocalDate.now());

        medicationRepository.save(medication);
        redirectAttributes.addFlashAttribute("message", "Medication saved successfully.");
        return "redirect:/doctor/dashboard";
    }

    @GetMapping("/medications/view/{patientId}")
    public String viewPatientMedications(
            @PathVariable Long patientId,
            Model model) {
        List<MedicationDTO> medications = medicationService.getMedicationsByPatient(patientId);
        model.addAttribute("medications", medications);
        return "view_medicines";
    }

    @GetMapping("/cancelTheAppointment/{appointmentId}")
    public String cancelAppointment(@PathVariable Long appointmentId, RedirectAttributes redirectAttributes) {

        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();
        appointment.setStatus(true);
        appointmentRepository.save(appointment);

        AppointmentSlot slot = appointment.getSlot();
        if (slot != null) {
            slot.setBooked(false);
            appointmentSlotRepository.save(slot);
        } else {
            redirectAttributes.addFlashAttribute("error", "No slot assigned to this appointment.");
        }

        redirectAttributes.addFlashAttribute("message", "Appointment cancelled and slot reopened.");
        return "redirect:/doctor/dashboard";

    }


}
