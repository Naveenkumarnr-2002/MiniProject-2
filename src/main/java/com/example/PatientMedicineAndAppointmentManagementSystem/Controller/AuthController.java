package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.PatientDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.Authenticator;

@Tag(
        name = "permission"

)
@Controller
@AllArgsConstructor
public class AuthController {

    private PatientService patientService;

    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;


    @Operation(
            summary = "it open the login page"
    )
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "success", required = false) String success, Model model) {
        if (error != null) {
            model.addAttribute("errorMsg", "Invalid email or password.");
        }
        if (success != null) {
            model.addAttribute("successMsg", "Account was created successfully!!!");
        }
        return "loginPages";
    }

    @GetMapping("/register")
    public String register() {
        return "registerButtons";
    }


}
