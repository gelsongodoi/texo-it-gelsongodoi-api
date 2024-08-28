package br.com.texoit.movie.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorField {
    private int status;
    private String message;
    private LocalDateTime datetime;

    public ErrorField() {
    }

    public ErrorField(int status, String message) {
        this.status = status;
        this.message = message;
        this.datetime = LocalDateTime.now();
    }
}
