package br.com.texoit.movie.dto;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MovieProducersDto {

    private String nameProducer;

    private int yearMovie;

    public MovieProducersDto(String nameProducer, int yearMovie) {
        this.nameProducer = nameProducer;
        this.yearMovie = yearMovie;
    }
}
