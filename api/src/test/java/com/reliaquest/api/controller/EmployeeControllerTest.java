package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Test
    void getAllEmployees() {
        when(employeeService.getAllEmployees()).thenReturn(List.of());
        ResponseEntity<List<Employee>> allEmployees = employeeController.getAllEmployees();
        assertNotNull(allEmployees);
        assertTrue(CollectionUtils.isEmpty(allEmployees.getBody()));
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void nameSearch() {
        ResponseEntity<List<Employee>> abc = employeeController.getEmployeesByNameSearch("abc");
        assertEquals(0, Objects.requireNonNull(abc.getBody()).size());
    }
}
