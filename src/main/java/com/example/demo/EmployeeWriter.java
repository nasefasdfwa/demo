package com.example.demo;

import java.util.Collection;
import java.util.stream.Collectors;

import com.example.demo.domain.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmployeeWriter {

    private static final String INSERT_QUERY = "INSERT INTO EMPLOYEE (first_name, last_name, address) VALUES (?, ?, ?)";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Employee writeEmployee(Employee employee) {
        int id = writeEmployee(employee.getFirstName(), employee.getLastName(), employee.getAddress());
        return employee.setId(id);
    }

    public Collection<Employee> writeEmployees(Collection<Employee> employees) {
        return employees.stream()
                .map(this::writeEmployee)
                .collect(Collectors.toList());
    }

    @Transactional
    private int writeEmployee(String firstName, String lastName, String address) {
        return jdbcTemplate.update(INSERT_QUERY, firstName, lastName, address);
    }
}
