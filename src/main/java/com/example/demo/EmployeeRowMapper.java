package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.example.demo.domain.Employee;
import org.springframework.jdbc.core.RowMapper;

public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee()
                .setId(rs.getInt("ID"))
                .setFirstName(rs.getString("FIRST_NAME"))
                .setLastName(rs.getString("LAST_NAME"))
                .setAddress(rs.getString("ADDRESS"));
    }
}
