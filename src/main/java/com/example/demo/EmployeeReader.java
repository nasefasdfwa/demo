package com.example.demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.example.demo.domain.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeeReader {

    private static final Path path = new Path("/data/reports");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FileSystem fileSystem;

    public Employee readEmployee(String id) {
        return jdbcTemplate.queryForObject("SELECT * FROM employee WHERE id = " + id, new EmployeeRowMapper());
    }

    public Collection<Employee> readEmployees(String... ids) {
        return Arrays.stream(ids).map(this::readEmployee).collect(Collectors.toSet());
    }

    public String readEmployeesAsJson(String... ids) throws Exception {
        return objectMapper.writer().writeValueAsString(readEmployees(ids));
    }

    public InputStream readReport(String reportName) throws Exception {
        return new ByteArrayInputStream(IOUtils.toByteArray(fileSystem.open(new Path(path, reportName))));
    }
}
