package com.osi.hris.service;

import java.time.LocalDate;
import java.util.List;

import com.osi.hris.entity.Attendance;

public interface AttendanceService {

    Attendance markAttendance(Attendance attendance);

    List<Attendance> getAllAttendance();

    Attendance getAttendanceByEmployeeAndDate(Long empId, LocalDate date);

    List<Attendance> getAttendanceByEmployeeId(Long empId);
}
