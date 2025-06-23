package com.example.PatientMedicineAndAppointmentManagementSystem.Controller;


import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentDTO;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "created and delete operation"

)
@Controller
@RequiredArgsConstructor
@RequestMapping("/appointment")
public class AppointmentController
{
    private final AppointmentService appointmentService;

    @Operation(
            summary = "it store the appointment  in database"
    )
    @PostMapping("/create")
    public String createAppointment(@ModelAttribute AppointmentDTO appointmentDto) {
        appointmentService.saveAppointment(appointmentDto);
        return "redirect:/patient/dashboard";
    }

    @Operation(
            summary = "it deleted the appointment by using id for the appointment database"
    )
    @PostMapping("/delete/{id}")
    public String deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return "redirect:/patient/dashboard";
    }
}
