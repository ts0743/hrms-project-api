package com.osi.hris.service;

import java.util.List;
import com.osi.hris.dto.LeaveRequestDTO;
import com.osi.hris.dto.LeaveResponseDTO;

public interface LeaveService {

    LeaveResponseDTO applyLeave(LeaveRequestDTO leaveRequest);

    List<LeaveResponseDTO> getAllLeaves();

    LeaveResponseDTO getLeaveById(Long id);

    LeaveResponseDTO approveLeave(Long id);

    LeaveResponseDTO rejectLeave(Long id, String reason); 

    List<LeaveResponseDTO> getLeavesByEmployeeId(Long empId);

}
