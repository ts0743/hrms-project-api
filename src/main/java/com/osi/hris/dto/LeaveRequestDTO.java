package com.osi.hris.dto;

import java.time.LocalDate;

public class LeaveRequestDTO {

    private Long employeeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String leaveType;
    private String reason;

    // Optional: leave balance at the time of request (used internally)
    private Integer availableBalance;

    public LeaveRequestDTO() {}

    public LeaveRequestDTO(Long employeeId, LocalDate startDate, LocalDate endDate, String leaveType, String reason) {
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.leaveType = leaveType;
        this.reason = reason;
    }

    // Getters & Setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Integer getAvailableBalance() { return availableBalance; }
    public void setAvailableBalance(Integer availableBalance) { this.availableBalance = availableBalance; }
}
