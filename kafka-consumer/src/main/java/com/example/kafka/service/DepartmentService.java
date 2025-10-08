package com.example.kafka.service;

import com.example.kafka.entity.Department;
import com.example.kafka.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @Transactional
    public Long getDepartmentId(String name) {
        Department department = departmentRepository.findByName(name);

        if(department != null)
            return department.getId();

        Department newDepartment = Department.builder().name(name).build();

        departmentRepository.save(newDepartment);

        return newDepartment.getId();
    }
}
