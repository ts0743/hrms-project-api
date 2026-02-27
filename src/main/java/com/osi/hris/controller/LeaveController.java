package com.osi.hris.controller;

import com.osi.hris.dto.LeaveRequestDTO;
import com.osi.hris.dto.LeaveResponseDTO;
import com.osi.hris.exception.BadRequestException;
import com.osi.hris.service.LeaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Leave")
public class LeaveController {

    private static final Logger logger = LoggerFactory.getLogger(LeaveController.class);
    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Apply for leave
    @PostMapping
    public LeaveResponseDTO applyLeave(@RequestBody LeaveRequestDTO dto) {
        logger.info("POST /leaves called for employeeId={}", dto.getEmployeeId());
        if (dto == null || dto.getEmployeeId() == null) {
            logger.error("Invalid leave request: missing employeeId");
            throw new BadRequestException("Employee ID is required");
        }
        return leaveService.applyLeave(dto);
    }

    // Approve leave
    @PutMapping("/approve/{id}")
    public LeaveResponseDTO approveLeave(@PathVariable Long id) {
        logger.info("PUT /leaves/approve/{} called", id);
        if (id == null) {
            logger.error("Leave ID is null for approval");
            throw new BadRequestException("Leave ID is required for approval");
        }
        return leaveService.approveLeave(id);
    }

    // Reject leave
    @PutMapping("/reject/{id}")
    public LeaveResponseDTO rejectLeave(@PathVariable Long id, @RequestParam String reason) {
        logger.info("PUT /leaves/reject/{} called with reason={}", id, reason);
        if (id == null) {
            logger.error("Leave ID is null for rejection");
            throw new BadRequestException("Leave ID is required for rejection");
        }
        if (reason == null || reason.isEmpty()) {
            logger.error("Rejection reason is missing for leaveId={}", id);
            throw new BadRequestException("Rejection reason is required");
        }
        return leaveService.rejectLeave(id, reason);
    }

    // Get leave by ID
    @GetMapping("/{id}")
    public LeaveResponseDTO getLeaveById(@PathVariable Long id) {
        logger.info("GET /leaves/{} called", id);
        if (id == null) {
            logger.error("Leave ID is null for fetching leave");
            throw new BadRequestException("Leave ID is required");
        }
        return leaveService.getLeaveById(id);
    }

    // Get all leaves
    @GetMapping
    public List<LeaveResponseDTO> getAllLeaves() {
        logger.info("GET /leaves called");
        return leaveService.getAllLeaves();
    }

    // Get leaves by employee
    @GetMapping("/employee/{empId}")
    public List<LeaveResponseDTO> getLeavesByEmployee(@PathVariable Long empId) {
        logger.info("GET /leaves/employee/{} called", empId);
        if (empId == null) {
            logger.error("Employee ID is null for fetching leaves");
            throw new BadRequestException("Employee ID is required");
        }
        return leaveService.getLeavesByEmployeeId(empId);
    }
}
