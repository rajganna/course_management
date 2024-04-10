package com.minden.course_management_system.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class StudentAlreadySignedUpException extends RuntimeException {
    public StudentAlreadySignedUpException(String message) {
        super(message);
    }
}
