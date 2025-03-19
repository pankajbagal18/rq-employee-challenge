package com.reliaquest.api.controller;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "mock-employee-client", url = "${base.mock.employee.url}")
public interface MockEmployeeClient {

    @GetMapping()
    Response<List<Employee>> getAllEmployees();
}
