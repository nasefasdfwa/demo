package com.example.demo.rest;

import java.util.Collection;

import com.example.demo.EmployeeReader;
import com.example.demo.EmployeeWriter;
import com.example.demo.domain.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(EmployeeController.ROOT_PATH)
public class EmployeeController {

    @Autowired
    private EmployeeReader employeeReader;

    @Autowired
    private EmployeeWriter employeeWriter;

    @Autowired
    private ObjectMapper objectMapper;

    public static final String ROOT_PATH = "employee";

    @GetMapping("/by/ids")
    public String getEmployees(@RequestParam("ids") Collection<String> ids) throws Exception {
        return employeeReader.readEmployeesAsJson(ids.toArray(new String[0]));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String saveNewEmployee(@RequestBody Employee employee) throws Exception {
        return objectMapper.writer().writeValueAsString(employeeWriter.writeEmployee(employee));
    }

    @GetMapping("/{reportName}")
    public ResponseEntity<Resource> getReport(@PathVariable() String reportName) throws Exception {
        return ResponseEntity
                .ok()
                .body(new InputStreamResource(employeeReader.readReport(reportName)));
    }
}
