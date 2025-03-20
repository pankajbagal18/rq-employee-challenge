package com.reliaquest.api.controller;

import com.reliaquest.api.model.CreateEmployeeInput;
import com.reliaquest.api.model.DeleteEmployeeInput;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.Response;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mock-employee-client", url = "${base.mock.employee.url}")
public interface MockEmployeeClient {

    // using cache for all users might not be a great idea
    // but with restrictions on server it is nice to limit the calls for getAllUser
    @GetMapping()
    @Cacheable("allEmployees")
    Response<List<Employee>> getAllEmployees();

    @GetMapping("/{id}")
    ResponseEntity<Response<Employee>> findEmployeeById(@PathVariable String id);

    @PostMapping()
    @CacheEvict(value = "allEmployees", allEntries = true)
    Response<Employee> createEmployee(@Valid @RequestBody CreateEmployeeInput input);

    @DeleteMapping()
    @CacheEvict(value = "allEmployees", allEntries = true)
    Response<Boolean> deleteEmployee(@Valid @RequestBody DeleteEmployeeInput input);
}
