package com.reliaquest.api.service;

import com.reliaquest.api.controller.MockEmployeeClientTest;
import com.reliaquest.api.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EmployeeServiceTest {

    private final EmployeeService employeeService = new EmployeeService(new MockEmployeeClientTest.TestMockEmployeeClient());

    @Test
    void getAllEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertNotNull(allEmployees);
        assertEquals(1, allEmployees.size());
    }
}