package com.osi.hris.controller;

import com.osi.hris.dto.PerformanceRequestDTO;
import com.osi.hris.dto.PerformanceResponseDTO;
import com.osi.hris.service.PerformanceService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Performance")
public class PerformanceController {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceController.class);

    private final PerformanceService performanceService;

    public PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @PostMapping
    public ResponseEntity<PerformanceResponseDTO> addPerformanceReview(
            @Valid @RequestBody PerformanceRequestDTO requestDTO) {
        logger.info("Add Performance Review API called for employeeId={}", requestDTO.getEmployeeId());
        PerformanceResponseDTO responseDTO = performanceService.addPerformanceReview(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PerformanceResponseDTO>> getAllPerformanceReviews() {
        logger.info("Get All Performance Reviews API called");
        List<PerformanceResponseDTO> list = performanceService.getAllPerformanceReviews();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/{empId}")
    public ResponseEntity<List<PerformanceResponseDTO>> getPerformanceByEmployee(
            @PathVariable Long empId) {
        logger.info("Get Performance API called for employeeId={}", empId);
        List<PerformanceResponseDTO> list = performanceService.getPerformanceByEmployeeId(empId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/employee/{empId}/year/{year}")
    public ResponseEntity<PerformanceResponseDTO> getPerformanceByEmployeeAndYear(
            @PathVariable Long empId,
            @PathVariable int year) {
        logger.info("Get Performance API called for employeeId={} year={}", empId, year);
        PerformanceResponseDTO dto = performanceService.getPerformanceByEmployeeAndYear(empId, year);
        if (dto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(dto);
    }
}


   
