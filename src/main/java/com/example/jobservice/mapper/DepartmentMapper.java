package com.example.jobservice.mapper;

import com.example.jobservice.vo.Department;
import org.springframework.data.repository.query.Param;

public interface DepartmentMapper {

    Department findByName(@Param("name") String name);
    void insert(Department name);
}
