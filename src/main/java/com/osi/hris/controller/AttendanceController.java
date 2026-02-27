package com.osi.hris.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.osi.hris.entity.Attendance;
import com.osi.hris.exception.ResourceNotFoundException;
import com.osi.hris.service.AttendanceService;

@RestController
@RequestMapping("/Attendance")
public class AttendanceController {
    private final AttendanceService attendanceService;
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }
    @PostMapping("/mark")
    public Attendance markAttendance(@RequestBody Attendance attendance) {
        return attendanceService.markAttendance(attendance);
    }
    @GetMapping
    public List<Attendance> getAllAttendance() {
        return attendanceService.getAllAttendance();
    }
    @GetMapping("/employee/{empId}")
    public List<Attendance> getByEmployee(@PathVariable Long empId) {
        return attendanceService.getAttendanceByEmployeeId(empId);
    }
    @GetMapping("/employee/{empId}/date/{date}")
    public Attendance getByEmployeeAndDate(
            @PathVariable Long empId,
            @PathVariable LocalDate date) {
        return attendanceService.getAttendanceByEmployeeAndDate(empId, date);
    }
    @GetMapping("/employee/{empId}/month/{month}/year/{year}")
    public List<Attendance> getAttendanceByEmployeeMonthYear(
            @PathVariable Long empId,
            @PathVariable int month,
            @PathVariable int year) {
    List<Attendance> allAttendance = attendanceService.getAttendanceByEmployeeId(empId);
        List<Attendance> filtered = allAttendance.stream()
                .filter(a -> a.getDate().getMonthValue() == month &&
                             a.getDate().getYear() == year)
                .toList();
       if (filtered.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No attendance found for employeeId=" + empId +
                    " in month=" + month + " and year=" + year);
        }

        return filtered;
    }
}

