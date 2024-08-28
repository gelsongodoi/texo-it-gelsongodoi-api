package br.com.texoit.movie.dto;

import br.com.texoit.movie.model.Movie;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class MovieResponseDto {

    private int year;

    private String title;

    private String studio;

    private String producer;

    private String winner;

    public MovieResponseDto(Movie movie) {
        this.year = movie.getYearMovie();
        this.title = movie.getTitle();
        this.studio = movie.getStudio();
        this.producer = movie.getProducer();
        this.winner = movie.getWinner();
    }

    public static List<MovieResponseDto> converterList(List<Movie> movies) {
        List<MovieResponseDto> movieResponseDtos = new ArrayList<>();
        for (Movie movie : movies) {
            MovieResponseDto movieResponseDto = new MovieResponseDto(movie);
            movieResponseDtos.add(movieResponseDto);
        }
        return movieResponseDtos;
    }
}
