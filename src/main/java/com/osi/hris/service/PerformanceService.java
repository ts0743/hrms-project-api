package com.osi.hris.service;

import com.osi.hris.dto.PerformanceRequestDTO;
import com.osi.hris.dto.PerformanceResponseDTO;

import java.util.List;

public interface PerformanceService {

    PerformanceResponseDTO addPerformanceReview(PerformanceRequestDTO requestDTO);

    List<PerformanceResponseDTO> getAllPerformanceReviews();

    List<PerformanceResponseDTO> getPerformanceByEmployeeId(Long employeeId);

    PerformanceResponseDTO getPerformanceByEmployeeAndYear(Long empId, int year);
}
