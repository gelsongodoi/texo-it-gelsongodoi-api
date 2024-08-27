package br.com.texoit.movie.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class MovieRequestDto {

    @CsvBindByName(column = "year")
    private int yearMovie;

    @CsvBindByName(column = "title")
    private String title;

    @CsvBindByName(column = "studios")
    private String studio;

    @CsvBindByName(column = "producers")
    private String producer;

    @CsvBindByName(column = "winner")
    private String winner;

    public MovieRequestDto(int yearMovie, String title, String studio, String producer, String winner) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
        this.winner = winner;
    }
}
