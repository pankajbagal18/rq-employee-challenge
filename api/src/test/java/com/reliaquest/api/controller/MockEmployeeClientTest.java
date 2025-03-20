package com.reliaquest.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MockEmployeeClientTest {
    public static List<Employee> EMPLOYEE_LIST = new ArrayList<>(List.of(
            Employee.from(
                    "test@mymail.com",
                    CreateEmployeeInput.builder()
                            .name("John")
                            .salary(100)
                            .title("Mr")
                            .age(34)
                            .build()),
            Employee.from(
                    "abc@f.com",
                    CreateEmployeeInput.builder()
                            .name("Kim")
                            .salary(200)
                            .title("Ms")
                            .age(40)
                            .build()),
            Employee.from(
                    "bgh@f.com",
                    CreateEmployeeInput.builder()
                            .name("Jonathan")
                            .salary(75)
                            .age(28)
                            .build())));

    public static final class TestMockEmployeeClient implements MockEmployeeClient {
        @Override
        public Response<List<Employee>> getAllEmployees() {
            return Response.from(EMPLOYEE_LIST);
        }

        @Override
        public ResponseEntity<Response<Employee>> findEmployeeById(String id) {
            return EMPLOYEE_LIST.stream()
                    .filter(emp -> emp.getId().toString().equals(id))
                    .findFirst()
                    .map(employee -> ResponseEntity.ok(Response.from(employee)))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        }

        @Override
        public Response<Employee> createEmployee(CreateEmployeeInput input) {
            Employee createdEmployee = Employee.from(input.getName() + "@m.com", input);
            EMPLOYEE_LIST.add(createdEmployee);
            return Response.from(createdEmployee);
        }

        @Override
        public Response<Boolean> deleteEmployee(DeleteEmployeeInput input) {
            Optional<Employee> employeeToDelete = EMPLOYEE_LIST.stream()
                    .filter(employee -> employee.getName().equalsIgnoreCase(input.getName()))
                    .findFirst();
            return Response.from(employeeToDelete.map(EMPLOYEE_LIST::remove).orElse(false));
        }
    }

    private final TestMockEmployeeClient testMockEmployeeClient = new TestMockEmployeeClient();

    @Test
    void getAllEmployees() {
        Response<List<Employee>> allEmployees = testMockEmployeeClient.getAllEmployees();
        assertEquals(EMPLOYEE_LIST.size(), allEmployees.data().size());
        assertEquals("test@mymail.com", allEmployees.data().get(0).getEmail());
    }

    @Test
    void findEmployeeById() {
        ResponseEntity<Response<Employee>> employeeById = testMockEmployeeClient.findEmployeeById(
                EMPLOYEE_LIST.get(2).getId().toString());
        assertEquals(
                EMPLOYEE_LIST.get(2).getEmail(),
                Objects.requireNonNull(employeeById.getBody()).data().getEmail());
    }
}
