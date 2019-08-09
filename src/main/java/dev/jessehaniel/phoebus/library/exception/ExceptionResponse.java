package dev.jessehaniel.phoebus.library.exception;

import java.time.LocalDateTime;

public class ExceptionResponse {
    private LocalDateTime timeOccurrence = LocalDateTime.now();
    private String exceptionMessage;
    private String details;
    
    ExceptionResponse(String exceptionMessage, String details) {
        this.exceptionMessage = exceptionMessage;
        this.details = details;
    }
    
    public LocalDateTime getTimeOccurrence() {
        return timeOccurrence;
    }
    
    public String getExceptionMessage() {
        return exceptionMessage;
    }
    
    public String getDetails() {
        return details;
    }
}
