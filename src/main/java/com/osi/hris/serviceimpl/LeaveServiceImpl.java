package com.osi.hris.serviceimpl;

import com.osi.hris.dto.LeaveRequestDTO;
import com.osi.hris.dto.LeaveResponseDTO;
import com.osi.hris.entity.Leave;
import com.osi.hris.exception.BadRequestException;
import com.osi.hris.exception.LeaveBalanceInsufficientException;
import com.osi.hris.exception.ResourceNotFoundException;
import com.osi.hris.repository.LeaveRepository;
import com.osi.hris.service.LeaveService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaveServiceImpl implements LeaveService {

    private static final Logger log = LoggerFactory.getLogger(LeaveServiceImpl.class);

    private final LeaveRepository leaveRepository;

    // Mocked method for leave balance
    private final int DEFAULT_LEAVE_BALANCE = 15; // e.g., 15 days per employee

    public LeaveServiceImpl(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    @Override
    public LeaveResponseDTO applyLeave(LeaveRequestDTO dto) {
        log.info("Applying leave for employeeId={}, startDate={}, endDate={}", 
                  dto.getEmployeeId(), dto.getStartDate(), dto.getEndDate());

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            log.warn("Invalid leave dates: endDate before startDate for employeeId={}", dto.getEmployeeId());
            throw new BadRequestException("End date cannot be before start date");
        }

        // Check overlapping leaves
        boolean overlapping = leaveRepository.findAllByEmployeeId(dto.getEmployeeId())
                .stream()
                .anyMatch(l -> !l.getStatus().equals("REJECTED") &&
                        !(dto.getEndDate().isBefore(l.getStartDate()) || dto.getStartDate().isAfter(l.getEndDate())));
        if (overlapping) {
            log.warn("Overlapping leave detected for employeeId={}", dto.getEmployeeId());
            throw new BadRequestException("Employee already has leave during these dates");
        }

        // Calculate leave days requested
        long daysRequested = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;

        // Check leave balance
        int usedLeaves = leaveRepository.findAllByEmployeeId(dto.getEmployeeId())
                .stream()
                .filter(l -> l.getStatus().equals("APPROVED"))
                .mapToInt(l -> (int) ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();

        int availableBalance = DEFAULT_LEAVE_BALANCE - usedLeaves;
        log.info("EmployeeId={} has {} days available balance", dto.getEmployeeId(), availableBalance);

        if (daysRequested > availableBalance) {
            log.warn("Insufficient leave balance for employeeId={}", dto.getEmployeeId());
            throw new LeaveBalanceInsufficientException(
                    "Insufficient leave balance. Requested: " + daysRequested + ", Available: " + availableBalance
            );
        }

        Leave leave = new Leave(dto.getEmployeeId(), dto.getStartDate(), dto.getEndDate(),
                                dto.getLeaveType(), "PENDING", dto.getReason());
        leave = leaveRepository.save(leave);

        log.info("Leave applied successfully with id={} for employeeId={}", leave.getId(), dto.getEmployeeId());

        return mapToDTO(leave);
    }

    @Override
    public List<LeaveResponseDTO> getAllLeaves() {
        log.info("Fetching all leave records");
        List<LeaveResponseDTO> leaves = leaveRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Found {} leave records", leaves.size());
        return leaves;
    }

    @Override
    public LeaveResponseDTO getLeaveById(Long id) {
        log.info("Fetching leave with id={}", id);
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Leave not found with id={}", id);
                    return new ResourceNotFoundException("Leave not found with id=" + id);
                });
        return mapToDTO(leave);
    }

    @Override
    public LeaveResponseDTO approveLeave(Long id) {
        log.info("Approving leave with id={}", id);
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Leave not found for approval with id={}", id);
                    return new ResourceNotFoundException("Leave not found with id=" + id);
                });

        leave.setStatus("APPROVED");
        leaveRepository.save(leave);

        log.info("Leave approved successfully for id={}", id);
        return mapToDTO(leave);
    }

    @Override
    public LeaveResponseDTO rejectLeave(Long id, String reason) {
        log.info("Rejecting leave with id={} for reason={}", id, reason);
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Leave not found for rejection with id={}", id);
                    return new ResourceNotFoundException("Leave not found with id=" + id);
                });

        leave.setStatus("REJECTED");
        leave.setReason(reason);
        leaveRepository.save(leave);

        log.info("Leave rejected successfully for id={}", id);
        return mapToDTO(leave);
    }

    @Override
    public List<LeaveResponseDTO> getLeavesByEmployeeId(Long empId) {
        log.info("Fetching leaves for employeeId={}", empId);
        List<Leave> leaves = leaveRepository.findAllByEmployeeId(empId);

        if (leaves.isEmpty()) {
            log.warn("No leaves found for employeeId={}", empId);
            throw new ResourceNotFoundException("No leaves found for employeeId=" + empId);
        }

        log.info("Found {} leaves for employeeId={}", leaves.size(), empId);
        return leaves.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    private LeaveResponseDTO mapToDTO(Leave leave) {
        LeaveResponseDTO dto = new LeaveResponseDTO();
        dto.setId(leave.getId());
        dto.setEmployeeId(leave.getEmployeeId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setLeaveType(leave.getLeaveType());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        // Calculate used days
        int usedDays = (int) ChronoUnit.DAYS.between(leave.getStartDate(), leave.getEndDate()) + 1;
        dto.setUsedBalance(usedDays);
        return dto;
    }
}
