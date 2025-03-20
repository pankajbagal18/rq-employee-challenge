package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.reliaquest.api.controller.MockEmployeeClientTest;
import com.reliaquest.api.model.Employee;
import java.util.List;
import org.junit.jupiter.api.Test;

class EmployeeServiceTest {

    private final EmployeeService employeeService =
            new EmployeeService(new MockEmployeeClientTest.TestMockEmployeeClient());

    @Test
    void getAllEmployees() {
        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertNotNull(allEmployees);
        assertEquals(MockEmployeeClientTest.EMPLOYEE_LIST.size(), allEmployees.size());
    }

    @Test
    void searchByName() {
        List<Employee> employees = employeeService.findEmployeesByNameSearch("Jo");
        assertEquals(2, employees.size());
        assertEquals("John", employees.get(0).getName());
    }

    @Test
    void findHighestSalary() {
        Integer highestSalaryOfEmployees = employeeService.findHighestSalaryOfEmployees();
        assertEquals(200, highestSalaryOfEmployees);
    }

    @Test
    void findNHighestSalaried() {
        List<String> topSalariedEmployee = employeeService.findTopSalariedEmployee(2);
        assertEquals(2, topSalariedEmployee.size());
        assertEquals(List.of("Kim", "John"), topSalariedEmployee);
    }
}
