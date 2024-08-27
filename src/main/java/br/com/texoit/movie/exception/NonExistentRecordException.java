package br.com.texoit.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NonExistentRecordException extends RuntimeException {
    public NonExistentRecordException(String message) {
        super(message);
    }
}
