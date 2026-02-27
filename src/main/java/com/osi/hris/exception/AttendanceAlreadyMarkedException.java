package com.osi.hris.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AttendanceAlreadyMarkedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AttendanceAlreadyMarkedException(String message) {
        super(message);
    }
}
