package com.osi.hris.exception;

public class InvalidAttendanceStatus extends RuntimeException {
   
	private static final long serialVersionUID = 1L;

	public InvalidAttendanceStatus(String message) {
        super(message);
    }
}
