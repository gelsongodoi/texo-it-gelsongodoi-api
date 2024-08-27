package br.com.texoit.movie.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseException {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private List<ErrorField> errors;

    public ResponseException() {
    }

    public ResponseException(int status, String error, String message, List<ErrorField> errors) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorField> getErrors() {
        return errors;
    }
}
