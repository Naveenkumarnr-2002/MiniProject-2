package com.example.PatientMedicineAndAppointmentManagementSystem.Service;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AdminDto;

public interface AdminService {

    AdminDto saveAdmin(AdminDto adminDto);

    AdminDto findAdminByEmailId(String emailId);

    AdminDto findById(Long id);
}
