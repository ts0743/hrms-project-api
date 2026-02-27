package com.osi.hris.service;

import com.osi.hris.entity.Employee;
import java.util.List;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(Long id);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long id);
}
