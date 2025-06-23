package com.example.PatientMedicineAndAppointmentManagementSystem.Service;


import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentSlotDto;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentSlotService {
    List<AppointmentSlotDto>getSlotByDoctor(Long doctorId);
    void saveSlot(AppointmentSlotDto slotDto);
    void deleteSlot(Long id);
}
