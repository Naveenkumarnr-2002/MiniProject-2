package com.example.PatientMedicineAndAppointmentManagementSystem.Configuration;

import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Admin;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Patient;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AdminRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private AdminRepository adminRepository;

    // Hardcoded admin credentials
    private static final String ADMIN_EMAIL = "admin@gmail.com";
    // bcrypt for "admin" (generate with BCryptPasswordEncoder)
    private static final String ADMIN_PASSWORD = "$2a$10$zCiHfRQfT/hjtS50HQVsVOC3NbrfscPuKzQUYTt.FYJwOGBENxKIu";

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Check for admin
        Admin admin=adminRepository.findByEmailId(email);
       if (admin!=null){
           return new User(admin.getEmailId(),admin.getPassword(),
                   Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN")));
       }
        // 2. Check for patient in DB
        Patient patient = patientRepository.findByPatientEmail(email);
        if (patient != null) {
            return new org.springframework.security.core.userdetails.User(
                    patient.getPatientEmail(),
                    patient.getPatientPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_PATIENT"))
            );
        }
        // 2. Check for Doctor in DB
        Doctor doctor = doctorRepository.findByDoctorEmail(email);
        if (doctor != null) {
            return new org.springframework.security.core.userdetails.User(
                    doctor.getDoctorEmail(),
                    doctor.getDoctorPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_DOCTOR"))
            );
        }
        throw new UsernameNotFoundException("User not found");
    }
}
