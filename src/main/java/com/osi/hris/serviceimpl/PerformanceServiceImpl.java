package com.osi.hris.serviceimpl;

import com.osi.hris.dto.PerformanceRequestDTO;
import com.osi.hris.dto.PerformanceResponseDTO;
import com.osi.hris.entity.Performance;
import com.osi.hris.repository.PerformanceRepository;
import com.osi.hris.service.PerformanceService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceServiceImpl.class);

    private final PerformanceRepository performanceRepository;

    public PerformanceServiceImpl(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    @Override
    public PerformanceResponseDTO addPerformanceReview(PerformanceRequestDTO requestDTO) {
        logger.info("Adding performance review for employeeId={}", requestDTO.getEmployeeId());

        // Map DTO → entity
        Performance performance = new Performance();
        BeanUtils.copyProperties(requestDTO, performance);

        Performance saved = performanceRepository.save(performance);

        // Map entity → response DTO
        PerformanceResponseDTO response = new PerformanceResponseDTO();
        BeanUtils.copyProperties(saved, response);
        logger.info("Performance review saved with id={}", saved.getId());
        return response;
    }

    @Override
    public List<PerformanceResponseDTO> getAllPerformanceReviews() {
        logger.info("Fetching all performance reviews");
        return performanceRepository.findAll().stream()
                .map(p -> {
                    PerformanceResponseDTO dto = new PerformanceResponseDTO();
                    BeanUtils.copyProperties(p, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<PerformanceResponseDTO> getPerformanceByEmployeeId(Long employeeId) {
        logger.info("Fetching performance reviews for employeeId={}", employeeId);
        return performanceRepository.findByEmployeeId(employeeId).stream()
                .map(p -> {
                    PerformanceResponseDTO dto = new PerformanceResponseDTO();
                    BeanUtils.copyProperties(p, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public PerformanceResponseDTO getPerformanceByEmployeeAndYear(Long empId, int year) {
        logger.info("Fetching performance for employeeId={} year={}", empId, year);
        Performance performance = performanceRepository.findByEmployeeIdAndYear(empId, year)
                .orElse(null);

        if (performance == null) return null;

        PerformanceResponseDTO dto = new PerformanceResponseDTO();
        BeanUtils.copyProperties(performance, dto);
        return dto;
    }
}
