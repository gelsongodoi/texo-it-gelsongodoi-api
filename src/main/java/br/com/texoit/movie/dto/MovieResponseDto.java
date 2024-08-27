package br.com.texoit.movie.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MovieResponseDto {

    private String producer;

    private long interval;

    private long previousWin;

    private long followingWin;

    public MovieResponseDto(String producer, long interval, long previousWin, long followingWin) {
        this.producer = producer;
        this.interval = interval;
        this.previousWin = previousWin;
        this.followingWin = followingWin;
    }

    public MovieResponseDto(String producer, int yearMovie) {
        this.producer = producer;
        this.previousWin = yearMovie;
    }
}
