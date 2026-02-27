package com.osi.hris.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.osi.hris.entity.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    Optional<Leave> findById(Long id);

    List<Leave> findAllByEmployeeId(Long empId);
}
