package dev.jessehaniel.phoebus.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandlerController {
    
    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ExceptionResponse> handleNotSuchElement(NoSuchElementException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex.getMessage(), "Id not found"), HttpStatus.NOT_FOUND);
    }
}
