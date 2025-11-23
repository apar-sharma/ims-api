package com.apsharma.ims_api.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getClass() + e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) throws JsonProcessingException {
        return  ResponseEntity.status(HttpStatus.CONFLICT).body("Data Integrity Violation "+ e.getMessage());
    }
}
