package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AdminDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentSlotDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.AppointmentSlot;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AdminService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentSlotService;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Tag(
        name = "add the doctors",
        description = "adding the doctor details hospital database and creating a account to doctor  "
)
@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private PasswordEncoder passwordEncoder;
    private DoctorService doctorService;
    private AdminService adminService;
    private AppointmentSlotService appointmentSlotService;


    @GetMapping("/registration")
    public String Registration(Model model) {
        AdminDto adminDto = new AdminDto();
        model.addAttribute("admin", adminDto);
        return "adminRegistration";
    }

    @PostMapping("/saveRegistration")
    public String saveRegistration(@ModelAttribute("admin") AdminDto adminDto, BindingResult bindingResult, Model model) {
        if (adminService.findAdminByEmailId(adminDto.getEmailId()) != null) {
            bindingResult.rejectValue("emailId", "email.exist", "This Email is Already registered. Please Use Another Email.");
        } else if (doctorService.findByEmail(adminDto.getEmailId()) != null) {
            bindingResult.rejectValue("emailId", "email.exist", "This email is already registered in customer. Please use another email.");
        }
        if (bindingResult.hasErrors()) {
            return "adminRegistration";
        }
        adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        adminService.saveAdmin(adminDto);

        return "redirect:/login?success";

    }


    @Operation(
            summary = "open the main dashboard of admin page it contains add doctors option"
    )
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        List<DoctorDto> doctors = doctorService.getAllDoctors();
        model.addAttribute("totaldoctors", doctors);
        return "admin-dashboard"; // Make sure you have admin-dashboard.html in templates
    }

    @Operation(
            summary = "in this areas it take details of the new doctor for admin"
    )

    @GetMapping("/adddoctors")
    public String adddocotor(Model model) {
        DoctorDto doctorDto = new DoctorDto();
        model.addAttribute("addDoctor", doctorDto);
        return "addDoctor";
    }

    @Operation(
            summary = "it save the new details for the doctor in doctor database"
    )
    @PostMapping("/savethedoctordetasils")
    public String savethedoctordetasils(@ModelAttribute("addDoctor") DoctorDto doctorDto, BindingResult
            bindingResult, Model model) {
        if (doctorService.findByEmail(doctorDto.getDoctorEmail()) != null) {
            bindingResult.rejectValue("doctorEmail", "email.exists", "This email is already used. Please use another email.");
        }
        if (bindingResult.hasErrors()) {
            return "addDoctor";
        }
        doctorDto.setDoctorPassword(passwordEncoder.encode(doctorDto.getDoctorPassword()));
        doctorService.saveDoctor(doctorDto);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/updateDoctor/{id}")
    public String updateDoctor(@PathVariable("id") Long id, @ModelAttribute("addDoctor") DoctorDto doctorDto, BindingResult bindingResult, Model model) {
        if (id == null) {
            // Handle error, redirect, or show message
            return "redirect:/admin/dashboard?error=invalid_id";
        }
        DoctorDto doctorDto1 = doctorService.findById(id);

        model.addAttribute("updateDoctor", doctorDto1);

        return "editDoctor";
    }

    @PostMapping("/saveTheUpdateDoctor")
    public String saveTheUpdateBus(@ModelAttribute("updateDoctor") DoctorDto doctorDto) {
        doctorService.updateDoctor(doctorDto.getDoctorId(), doctorDto);
        return "redirect:/admin/dashboard";
    }


    @GetMapping("/deleteDoctor/{id}")
    public String deleteDoctor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            doctorService.deleteDoctorById(id);
            redirectAttributes.addFlashAttribute("message", "Doctor Deleted Successfully");
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/doctor/{doctorId}/slots")
    public String viewSlots(@PathVariable Long doctorId, Model model) {
//        LocalDate today = LocalDate.now();
//        System.out.println("Querying slots for doctor " + doctorId + " and date: " + today);
//
//        List<AppointmentSlotDto> slots = appointmentSlotService.getSlotByDoctorAndDate(doctorId, today);
//        System.out.println("Slots: " + slots);
//
//        model.addAttribute("doctorId", doctorId);
//        model.addAttribute("slot", new AppointmentSlotDto());
//        model.addAttribute("slots",slots);
//        model.addAttribute("slots", appointmentSlotService.getSlotByDoctorAndDate(doctorId, LocalDate.now()));
//        List<AppointmentSlotDto> slots = appointmentSlotService.getSlotByDoctorAndDate(doctorId, LocalDate.now());
//        System.out.println("Slots: " + slots);
//        System.out.println("Saving slot with date: " + slots.getDate());

        LocalDate today=LocalDate.now();
        List<AppointmentSlotDto>slots=appointmentSlotService.getSlotByDoctor(doctorId);

        model.addAttribute("doctorId",doctorId);
        model.addAttribute("slot",new AppointmentSlotDto());
        model.addAttribute("slots",slots);

        return "doctor-slots";
    }

    @PostMapping("/doctor/{doctorId}/slotsList")
    public String addSlot(@PathVariable Long doctorId, @ModelAttribute AppointmentSlotDto slotDto) {
        slotDto.setDoctorId(doctorId);
        appointmentSlotService.saveSlot(slotDto);
        return "redirect:/admin/doctor/" + doctorId + "/slots";

    }

    @GetMapping("/slot/delete/{id}")
    public String deleteSlot(@PathVariable Long id, @RequestParam("doctorId") Long doctorId) {
        appointmentSlotService.deleteSlot(id);
        return "redirect:/admin/doctor/" + doctorId + "/slots";
    }


}
