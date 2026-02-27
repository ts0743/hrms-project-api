package com.osi.hris.dto;

import java.time.LocalDate;

import com.osi.hris.enums.AttendanceStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;

public class AttendanceRequestDTO {

    @NotNull(message = "Employee ID must not be null")
    private Long employeeId;

    @NotNull(message = "Date must not be null")
    private LocalDate date;

    @NotNull(message = "Attendance status must not be null")
    private AttendanceStatus status;

    @Max(value = 24, message = "Working hours cannot exceed 24")
    private Double workingHours;

    // Getters and Setters
    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }
}
