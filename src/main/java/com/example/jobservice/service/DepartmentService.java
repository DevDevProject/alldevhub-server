package com.example.jobservice.service;

import com.example.jobservice.mapper.DepartmentMapper;
import com.example.jobservice.vo.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentMapper departmentMapper;

    @Transactional
    public Long getDepartmentId(String name) {
        Department department = departmentMapper.findByName(name);

        if(department != null)
            return department.getId();

        Department newDepartment = Department.builder().name(name).build();

        departmentMapper.insert(newDepartment);

        return departmentMapper.findByName(name).getId();
    }
}
