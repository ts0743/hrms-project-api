package com.osi.hris.serviceimpl;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.osi.hris.entity.Attendance;
import com.osi.hris.exception.AttendanceAlreadyMarkedException;
import com.osi.hris.exception.BadRequestException;
import com.osi.hris.exception.ResourceNotFoundException;
import com.osi.hris.repository.AttendanceRepository;
import com.osi.hris.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private static final Logger logger =
            LoggerFactory.getLogger(AttendanceServiceImpl.class);

    private final AttendanceRepository attendanceRepository;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Attendance markAttendance(Attendance attendance) {

        if (attendance == null ||
            attendance.getEmployeeId() == null ||
            attendance.getDate() == null ||
            attendance.getStatus() == null) {

            throw new BadRequestException(
                    "employeeId, date and status must not be null");
        }

        logger.info("Marking attendance: empId={}, date={}, status={}",
                attendance.getEmployeeId(),
                attendance.getDate(),
                attendance.getStatus());

        // Prevent duplicate attendance
        if (attendanceRepository.existsByEmployeeIdAndDate(
                attendance.getEmployeeId(), attendance.getDate())) {

            throw new AttendanceAlreadyMarkedException(
                    "Attendance already marked for employeeId="
                            + attendance.getEmployeeId()
                            + " on date=" + attendance.getDate());
        }

        // Working hours validation
        if (attendance.getWorkingHours() != null &&
            attendance.getWorkingHours() > 24) {

            throw new BadRequestException(
                    "Working hours cannot exceed 24");
        }

        // Business rules based on status
        switch (attendance.getStatus()) {

            case PRESENT:
            case WFH:
                attendance.setWorkingHours(
                        attendance.getWorkingHours() != null
                                ? attendance.getWorkingHours()
                                : 8.0
                );
                break;

            case ABSENT:
            case HOLIDAY:
            case LEAVE:
                attendance.setWorkingHours(null);
                break;
        }

        Attendance saved = attendanceRepository.save(attendance);
        logger.info("Attendance saved successfully with id={}", saved.getId());
        return saved;
    }

    @Override
    public List<Attendance> getAllAttendance() {
        logger.info("Fetching all attendance records");
        return attendanceRepository.findAll();
    }

    @Override
    public Attendance getAttendanceByEmployeeAndDate(Long empId, LocalDate date) {
        logger.info("Fetching attendance for empId={} and date={}", empId, date);
        return attendanceRepository
                .findByEmployeeIdAndDate(empId, date)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Attendance not found for employeeId="
                                        + empId + " on date=" + date));
    }

    @Override
    public List<Attendance> getAttendanceByEmployeeId(Long empId) {
        List<Attendance> list =
                attendanceRepository.findAllByEmployeeId(empId);

        if (list.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No attendance found for employeeId=" + empId);
        }
        return list;
    }
}
