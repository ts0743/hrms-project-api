package com.osi.hris.serviceimpl;

import com.osi.hris.entity.Employee;
import com.osi.hris.exception.EmployeeAlreadyExistsException;
import com.osi.hris.exception.ResourceNotFoundException;
import com.osi.hris.repository.EmployeeRepository;
import com.osi.hris.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee addEmployee(Employee employee) {
        logger.info("addEmployee called with employee: {}", employee != null ? employee.getEmail() : null);

        if (employee == null) {
            logger.error("Employee object is null");
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            logger.error("Employee email is null or empty");
            throw new IllegalArgumentException("Employee email cannot be null or empty");
        }

        if (employeeRepository.existsByEmail(employee.getEmail())) {
            logger.warn("Employee already exists with email={}", employee.getEmail());
            throw new EmployeeAlreadyExistsException("Employee already exists with email=" + employee.getEmail());
        }

        Employee savedEmployee = employeeRepository.save(employee);
        logger.info("Employee saved successfully with id={}", savedEmployee.getId());
        return savedEmployee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        logger.info("getAllEmployees called");
        List<Employee> employees = employeeRepository.findAll();
        logger.info("Found {} employees", employees.size());
        return employees;
    }

    @Override
    public Employee getEmployeeById(Long id) {
        logger.info("getEmployeeById called with id={}", id);

        if (id == null) {
            logger.error("Employee ID is null");
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id={}", id);
                    return new ResourceNotFoundException("Employee not found with id=" + id);
                });

        logger.info("Employee found: {}", employee.getEmail());
        return employee;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        logger.info("updateEmployee called with id={}", employee != null ? employee.getId() : null);

        if (employee == null) {
            logger.error("Employee object is null");
            throw new IllegalArgumentException("Employee cannot be null");
        }

        if (employee.getId() == null) {
            logger.error("Employee ID is null");
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        Employee existingEmployee = employeeRepository.findById(employee.getId())
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id={}", employee.getId());
                    return new ResourceNotFoundException("Employee not found with id=" + employee.getId());
                });

        // Check for email duplication on update
        if (employee.getEmail() != null &&
                !employee.getEmail().equals(existingEmployee.getEmail()) &&
                employeeRepository.existsByEmail(employee.getEmail())) {

            logger.warn("Another employee already exists with email={}", employee.getEmail());
            throw new EmployeeAlreadyExistsException("Another employee already exists with email=" + employee.getEmail());
        }

        // Update fields
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setDesignation(employee.getDesignation());
        existingEmployee.setPhone(employee.getPhone());
        existingEmployee.setQualification(employee.getQualification());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        logger.info("Employee updated successfully with id={}", updatedEmployee.getId());

        return updatedEmployee;
    }

    @Override
    public void deleteEmployee(Long id) {
        logger.info("deleteEmployee called with id={}", id);

        if (id == null) {
            logger.error("Employee ID is null");
            throw new IllegalArgumentException("Employee ID cannot be null");
        }

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Employee not found with id={}", id);
                    return new ResourceNotFoundException("Employee not found with id=" + id);
                });

        employeeRepository.delete(existingEmployee);
        logger.info("Employee deleted successfully with id={}", id);
    }
}
