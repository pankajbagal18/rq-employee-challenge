package com.reliaquest.api.service;

import com.reliaquest.api.controller.MockEmployeeClient;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import feign.FeignException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final MockEmployeeClient mockEmployeeClient;

    public List<Employee> getAllEmployees() {
        return mockEmployeeClient.getAllEmployees().data();
    }

    public List<Employee> findEmployeesByNameSearch(String searchString) {
        Pattern pattern = Pattern.compile(".*" + searchString + ".*", Pattern.CASE_INSENSITIVE);
        List<Employee> allEmployees = getAllEmployees();
        return allEmployees.stream()
                .filter(emp -> pattern.matcher(emp.getName()).find())
                .collect(Collectors.toList());
    }

    public ResponseEntity<Employee> findEmployeeById(String id) {
        ResponseEntity<Response<Employee>> employeeById;
        try {
            employeeById = mockEmployeeClient.findEmployeeById(id);
        } catch (FeignException fe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(Objects.requireNonNull(employeeById.getBody()).data());
    }

    public Integer findHighestSalaryOfEmployees() {
        return getAllEmployees().stream()
                .map(Employee::getSalary)
                .max(Integer::compareTo)
                .orElse(-1);
    }

    public List<String> findTopSalariedEmployee(int depth) {
        return getAllEmployees().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(depth)
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    public Employee createNewEmployee(CreateEmployeeInput employeeInput) {
        return mockEmployeeClient.createEmployee(employeeInput).data();
    }

    public ResponseEntity<String> deleteEmployeeById(String id) {
        ResponseEntity<Employee> employeeById = findEmployeeById(id);
        if (!HttpStatus.NOT_FOUND.equals(employeeById.getStatusCode())) {
            Response<Boolean> deleted = mockEmployeeClient.deleteEmployee(DeleteEmployeeInput.builder()
                    .name(Objects.requireNonNull(employeeById.getBody()).getName())
                    .build());
            if (deleted.data()) {
                return ResponseEntity.ok("Employee deleted. Id - " + id);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee deletion failed. Id - " + id);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found with Id - " + id);
    }
}
