package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MockEmployeeClientTest {

    public static final class TestMockEmployeeClient implements MockEmployeeClient {
        List<Employee> employees = List.of(Employee.from("test@mymail.com", CreateEmployeeInput.builder().name("John").salary(100).title("Mr").age(34).build()));
        @Override
        public Response<List<Employee>> getAllEmployees() {
            return Response.from(employees);
        }
    }

    private final TestMockEmployeeClient testMockEmployeeClient = new TestMockEmployeeClient();

    @Test
    void getAllEmployees() {
        Response<List<Employee>> allEmployees = testMockEmployeeClient.getAllEmployees();
        assertEquals(1, allEmployees.data().size());
        assertEquals("test@mymail.com", allEmployees.data().get(0).getEmail());
    }
}