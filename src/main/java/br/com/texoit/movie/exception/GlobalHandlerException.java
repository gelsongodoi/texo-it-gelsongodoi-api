package br.com.texoit.movie.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        List<org.springframework.validation.FieldError> fieldErrors = result.getFieldErrors();

        List<ErrorField> errorFields = new ArrayList<>();

        for (org.springframework.validation.FieldError fieldError : fieldErrors) {
            ErrorField errorField = new ErrorField(fieldError.getField(), fieldError.getDefaultMessage());
            errorFields.add(errorField);
        }

        ResponseException responseException = new ResponseException(HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                errorFields);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseException);
    }
}
