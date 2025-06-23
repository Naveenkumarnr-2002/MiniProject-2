package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentHistoryDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.*;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.*;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentServiceHistory;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.DoctorService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Tag(
        name = "patient operation ",
        description = "in this code pattern operation work on it"
)

@Controller
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final PatientRepository patientRepository;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppointmentHistoryRepository appointmentHistoryRepository;
    private final AppointmentSlotRepository appointmentSlotRepository;
    private final AppointmentServiceHistory appointmentServiceHistory;
    private final AppointmentRepository appointmentRepository;


    @Operation(
            summary = "in this it take the registration page full details"
    )
    @GetMapping("/registration")
    public String Registration(Model model) {
        PatientDto patient = new PatientDto();
        model.addAttribute("patient", patient);
        return "registration";
    }


    @Operation(
            summary = "it saved the registration detail in to patient database"
    )
    @PostMapping("/saveRegistration")
    public String saveRegistration(@ModelAttribute("patient") PatientDto patientDto, BindingResult bindingResult, Model model) {
        if (patientService.findByEmail(patientDto.getPatientEmail()) != null) {
            bindingResult.rejectValue("patientEmail", "Email.exists", "This email is already registered. Please use another email.");
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        patientDto.setPatientPassword(passwordEncoder.encode(patientDto.getPatientPassword()));
        patientService.savePatient(patientDto);
        return "redirect:/login";
    }


    @Operation(
            summary = "it open ths dashboard of the patient with of make appointment and history"
    )
    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        PatientDto patient = patientService.findByEmail(email);
        model.addAttribute("patient", patient);
        List<Appointment> allAppointments = appointmentRepository.findByPatient_PatientId(patient.getPatientId());
        model.addAttribute("allAppointments", allAppointments);
        System.out.println("Appointments found: " + allAppointments.size());
        return "patient_dashboard";
    }

    @Operation(
            summary = "get the appoiment daetaisl"
    )

    @GetMapping("/make_appointment_page")
    public String appointmentForm(Long id, Authentication authentication, Model model) {
        String email = authentication.getName();
        PatientDto patient = patientService.findByEmail(email);
        model.addAttribute("patient", patient);
        List<DoctorDto> alldoctors = doctorService.getAllDoctors();
        model.addAttribute("alldoctors", alldoctors);
        return "make_Appointment";
    }

    @GetMapping("/make_appointment/{doctorId}")
    public String showAppointmentForm(@PathVariable Long doctorId, Model model) {
//        DoctorDto doctorDto=doctorService.findById(id);
//        model.addAttribute("doctor",doctorDto);
//        AppointmentDTO appointmentDTO=new AppointmentDTO();
//        appointmentDTO.setDoctorId(doctorDto.getDoctorId());
//        model.addAttribute("book_appointment",appointmentDTO);
//        return "finalBookingAppointmentPage";
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        List<AppointmentSlot> slots = appointmentSlotRepository.findByDoctorDoctorId(doctorId);

        model.addAttribute("doctor", doctor);
        model.addAttribute("availableSlots", slots);
        model.addAttribute("book_appointment", new AppointmentHistory());

        return "book_appointment";
    }

    @Operation(
            summary = "it save the appoint the data"
    )

    @PostMapping("/save_Appointment")
    public String saveAppointment(@RequestParam Long doctorId, @RequestParam String date, @RequestParam String time, Principal principal) {
//        String email = authentication.getName();
//        PatientDto patientDto = patientService.findByEmail(email);
//
//        // Set patientId (doctorId should already be set from the form)
//        appointmentDTO.setPatientId(patientDto.getPatientId());
//
//        appointmentService.saveAppointment(appointmentDTO);
//        appointmentServiceHistory.saveAppointment(appointmentDTO);
//        return "redirect:/patient/dashboard?success";

        Patient patient = patientRepository.findByPatientEmail(principal.getName());

        AppointmentSlot slot = appointmentSlotRepository.findByDoctorDoctorId(doctorId)
                .stream().filter(s -> s.getDate().toString().equals(date) && s.getTime().equals(time) && !s.isBooked())
                .findFirst().orElseThrow(() -> new IllegalStateException("Slot already booked or not found"));
        slot.setBooked(true);
        appointmentSlotRepository.save(slot);

//        AppointmentHistory history = new AppointmentHistory();
        Appointment appointment = new Appointment();
        appointment.setDoctor(slot.getDoctor());
        appointment.setPatient(patient);
        appointment.setPatientFirstName(patient.getPatientFirstName());
        appointment.setPatientLastName(patient.getPatientFirstName());
        appointment.setMedicalHistory(patient.getMedicalHistory());
        appointment.setPatientContact(patient.getPatientContact());
        appointment.setPatientEmail(patient.getPatientEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a").withLocale(Locale.ENGLISH).withResolverStyle(ResolverStyle.SMART);
        String timeTrimmed = slot.getTime().trim();
        appointment.setAppointmentDateTime(LocalDateTime.of(slot.getDate(), LocalTime.parse(timeTrimmed, formatter)));
        appointmentRepository.save(appointment);

//       Appointment appointment=new Appointment();
//       appointment.setDoctor(slot.getDoctor());
//       appointment.setAppointmentId(slot.getId());
//       appointment.setMedicalHistory(patient.getMedicalHistory());
//       appointment.setPatientContact(patient.getPatientContact());
//       appointment.setPatientEmail(patient.getPatientEmail());
//       appointment.setPatientLastName(patient.getPatientLastName());
//       appointment.setStatus(appointment.getStatus());
//       appointment.setPatientFirstName(patient.getPatientFirstName());
//        DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("hh:mm a").withLocale(Locale.ENGLISH).withResolverStyle(ResolverStyle.SMART);
//        String trim=slot.getTime().trim();
//        appointment.setAppointmentDateTime(LocalDateTime.of(slot.getDate(), LocalTime.parse(trim,dateTimeFormatter)));
//        appointmentRepository.save(appointment);

        return "redirect:/patient/dashboard";
    }

    @Operation(
            summary = "delete the appointment after the user deiced to deleted"
    )

    @GetMapping("/deleteAppointment/{id}")
    public String deleteAppointment(@PathVariable("id") Long id, Model model) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Invalid appointment Id: " + id));

        Doctor doctor = appointment.getDoctor();
        LocalDate date = appointment.getAppointmentDateTime().toLocalDate();
        LocalTime time = appointment.getAppointmentDateTime().toLocalTime();

        Optional<AppointmentSlot> optional = appointmentSlotRepository.findByDoctorAndDateAndTime(
                doctor, date, time.format(DateTimeFormatter.ofPattern("hh:mm a"))
        );
        if (optional.isPresent()) {
            AppointmentSlot slot = optional.get();
            slot.setBooked(false);
            appointmentSlotRepository.save(slot); // Enable the slot
        }
        appointmentRepository.deleteById(id);
        return "redirect:/patient/dashboard?delete";
    }

    @GetMapping("/history")
    public String history(Model model, Authentication authentication) {
        String email = authentication.getName();
        PatientDto patient = patientService.findByEmail(email);
        model.addAttribute("patient", patient);
        List<AppointmentHistoryDTO> appointments = appointmentServiceHistory.getAppointmentsByPatientEmail(email);
        model.addAttribute("allAppointments", appointments);
        return "history";
    }

    @GetMapping("/medicines/{patientId}")
    public String viewPrescribedMedicines(@PathVariable Long patientId, Model model) {
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        model.addAttribute("medicalHistory", patient.getMedicalHistory());
        return "view_medicines";
    }


}
