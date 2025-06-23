package com.example.PatientMedicineAndAppointmentManagementSystem.Service.imp;

import com.example.PatientMedicineAndAppointmentManagementSystem.Dto.AppointmentSlotDto;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.AppointmentSlot;
import com.example.PatientMedicineAndAppointmentManagementSystem.Entity.Doctor;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.AppointmentSlotRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Repository.DoctorRepository;
import com.example.PatientMedicineAndAppointmentManagementSystem.Service.AppointmentSlotService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentSlotServiceImpl implements AppointmentSlotService {

    private AppointmentSlotRepository slotRepository;
    private DoctorRepository doctorRepository;
    private ModelMapper mapper;

    @Override
    public List<AppointmentSlotDto> getSlotByDoctor(Long doctorId) {
        return slotRepository.findByDoctorDoctorId(doctorId)
                .stream().map(slot->{
                    AppointmentSlotDto dto=mapper.map(slot,AppointmentSlotDto.class);
                    dto.setDoctorId(slot.getDoctor().getDoctorId());
                    return dto;
                }).collect(Collectors.toList());
    }

    public void saveSlot(AppointmentSlotDto dto){
        AppointmentSlot slot=new AppointmentSlot();
        slot.setDate(dto.getDate());
        slot.setTime(dto.getTime());
        slot.setBooked(false);
        Doctor doctor=doctorRepository.findById(dto.getDoctorId())
                .orElseThrow();
        slot.setDoctor(doctor);
        slotRepository.save(slot);
    }

    public void deleteSlot(Long id){
        slotRepository.deleteById(id);
    }





















}
