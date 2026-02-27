package com.osi.hris.controller;

import com.osi.hris.entity.Employee;
import com.osi.hris.exception.BadRequestException;
import com.osi.hris.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Employee")
public class EmployeeController {
   private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
   private final EmployeeService employeeService;
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @PostMapping
    public Employee createEmployee(@RequestBody Employee employee) {
        logger.info("POST /employees called with employee: {}", employee != null ? employee.getEmail() : null);
            if (employee == null || employee.getEmail() == null || employee.getEmail().isEmpty()) {
            logger.error("Invalid request body or missing email");
            throw new BadRequestException("Employee email is required");
        }
        Employee savedEmployee = employeeService.addEmployee(employee);
        logger.info("Employee created successfully with id={}", savedEmployee.getId());
        return savedEmployee;
    }
    @GetMapping
    public List<Employee> getAllEmployees() {
        logger.info("GET /employees called");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Returning {} employees", employees.size());
        return employees;
    }
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        logger.info("GET /employees/{} called", id);

        if (id == null) {
            logger.error("Employee ID is null");
            throw new BadRequestException("Employee ID is required");
        }
        Employee employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            logger.warn("Employee not found with id={}", id);
            throw new BadRequestException("Employee not found with id=" + id);
        }
        logger.info("Employee found with id={}", id);
        return employee;
    }
    @PutMapping
    public Employee updateEmployee(@RequestBody Employee employee) {
        logger.info("PUT /employees called with id={}", employee != null ? employee.getId() : null);

        if (employee == null || employee.getId() == null) {
            throw new BadRequestException("Employee ID is required for update");
        }
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        logger.info("Employee updated successfully with id={}", updatedEmployee.getId());
        return updatedEmployee;
    }
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        logger.info("DELETE /employees/{} called", id);
            if (id == null) {
            logger.error("Employee ID is null");
            throw new BadRequestException("Employee ID is required for delete");
        }
        employeeService.deleteEmployee(id);
        logger.info("Employee deleted successfully with id={}", id);
        return "Employee deleted successfully";
    }
}
