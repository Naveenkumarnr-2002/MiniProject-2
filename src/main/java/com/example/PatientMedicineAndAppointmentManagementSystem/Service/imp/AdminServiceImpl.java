package com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AdminDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.DoctorDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Admin;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AdminRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AdminService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private ModelMapper modelMapper;
    private DoctorRepository doctorRepository;

    @Override
    public AdminDto saveAdmin(AdminDto adminDto) {
        Admin admin = modelMapper.map(adminDto, Admin.class);

        Admin savedAdmin = adminRepository.save(admin);

        return modelMapper.map(savedAdmin, AdminDto.class);
    }

    @Override
    public AdminDto findAdminByEmailId(String emailId) {

        Admin admin = adminRepository.findByEmailId(emailId);

        if (admin == null) {
            return null;
        }
        return modelMapper.map(admin, AdminDto.class);
    }

    @Override
    public AdminDto findById(Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow(() -> new RuntimeException("Doctor not found"));

        return modelMapper.map(admin, AdminDto.class);
    }

}
