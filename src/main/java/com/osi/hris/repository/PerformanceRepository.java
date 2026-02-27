package com.osi.hris.repository;

import com.osi.hris.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, Long> {

    default Optional<Performance> findByEmployeeIdAndYear(Long employeeId, int year) {
        return this.findAll().stream()
                .filter(p -> p.getEmployeeId().equals(employeeId) 
                          && p.getReviewDate().getYear() == year)
                .findFirst();
    }
    List<Performance> findByEmployeeId(Long employeeId);
}
