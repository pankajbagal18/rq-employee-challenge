package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reliaquest.api.controller.MockEmployeeClient;
import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import feign.FeignException;
import feign.Request;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceMockTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private MockEmployeeClient mockEmployeeClient;

    public static Stream<Arguments> deleteInput() {
        return Stream.of(
                Arguments.of(true, HttpStatus.OK, "Employee deleted. Id - 123"),
                Arguments.of(false, HttpStatus.BAD_REQUEST, "Employee deletion failed. Id - 123"));
    }

    @Test
    void addEmployee() {
        CreateEmployeeInput input = CreateEmployeeInput.builder().build();
        Mockito.when(mockEmployeeClient.createEmployee(input))
                .thenReturn(Response.from(Employee.builder().build()));
        employeeService.createNewEmployee(input);
        Mockito.verify(mockEmployeeClient, Mockito.times(1)).createEmployee(input);
    }

    @Test
    void deleteEmployee_EmployeeNotPresent() {
        Request request = Request.create(
                Request.HttpMethod.GET, "/ghas", Collections.emptyMap(), new byte[1], Charset.defaultCharset(), null);
        FeignException notFound =
                new FeignException.NotFound("Not found", request, new byte[1], Collections.emptyMap());
        Mockito.when(mockEmployeeClient.findEmployeeById("123")).thenThrow(notFound);
        ResponseEntity<String> deleted = employeeService.deleteEmployeeById("123");
        assertEquals(HttpStatus.NOT_FOUND, deleted.getStatusCode());
        assertEquals("Employee not found with Id - 123", deleted.getBody());
    }

    @Test
    void deleteEmployee_employeePresent_butFailed() {
        Mockito.when(mockEmployeeClient.findEmployeeById("123"))
                .thenReturn(ResponseEntity.ok(
                        Response.from(Employee.builder().name("MyName").build())));
        Mockito.when(mockEmployeeClient.deleteEmployee(Mockito.any(DeleteEmployeeInput.class)))
                .thenReturn(Response.from(false));
        ResponseEntity<String> deleted = employeeService.deleteEmployeeById("123");
        assertEquals(HttpStatus.BAD_REQUEST, deleted.getStatusCode());
        assertEquals("Employee deletion failed. Id - 123", deleted.getBody());
        Mockito.verify(mockEmployeeClient).findEmployeeById("123");
        Mockito.verify(mockEmployeeClient).deleteEmployee(Mockito.any(DeleteEmployeeInput.class));
    }

    @ParameterizedTest
    @MethodSource("deleteInput")
    void deleteEmployee_employeePresent(boolean shouldDelete, HttpStatus httpStatus, String message) {
        Mockito.when(mockEmployeeClient.findEmployeeById("123"))
                .thenReturn(ResponseEntity.ok(
                        Response.from(Employee.builder().name("MyName").build())));
        Mockito.when(mockEmployeeClient.deleteEmployee(Mockito.any(DeleteEmployeeInput.class)))
                .thenReturn(Response.from(shouldDelete));
        ResponseEntity<String> deleted = employeeService.deleteEmployeeById("123");
        assertEquals(httpStatus, deleted.getStatusCode());
        assertEquals(message, deleted.getBody());
        Mockito.verify(mockEmployeeClient).findEmployeeById("123");
        Mockito.verify(mockEmployeeClient).deleteEmployee(Mockito.any(DeleteEmployeeInput.class));
    }
}
