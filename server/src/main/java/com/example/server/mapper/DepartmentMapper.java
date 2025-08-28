package com.example.server.mapper;

import com.example.server.vo.Department;
import org.springframework.data.repository.query.Param;

public interface DepartmentMapper {

    Department findByName(@Param("name") String name);
    void insert(Department name);
}
