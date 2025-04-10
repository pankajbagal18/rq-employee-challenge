package com.reliaquest.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.*;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonNaming(Employee.PrefixNamingStrategy.class)
public class Employee {

    private UUID id;
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;

    public static Employee from(@NonNull String email, @NonNull CreateEmployeeInput input) {
        return Employee.builder()
                .id(UUID.randomUUID())
                .email(email)
                .name(input.getName())
                .salary(input.getSalary())
                .age(input.getAge())
                .title(input.getTitle())
                .build();
    }

    static class PrefixNamingStrategy extends PropertyNamingStrategies.NamingBase {

        @Override
        public String translate(String propertyName) {
            if ("id".equals(propertyName)) {
                return propertyName;
            }
            return "employee_" + propertyName;
        }
    }
}
