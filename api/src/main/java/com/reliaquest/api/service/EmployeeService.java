package com.reliaquest.api.service;

import com.reliaquest.api.controller.MockEmployeeClient;
import com.reliaquest.api.model.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final MockEmployeeClient mockEmployeeClient;

    public List<Employee> getAllEmployees() {
        return mockEmployeeClient.getAllEmployees().data();
    }
}
