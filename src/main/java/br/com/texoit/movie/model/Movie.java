package br.com.texoit.movie.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "tb_movie")
public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private int yearMovie;

    private String title;

    private String studio;

    private String producer;

    private String winner;

    public Movie() {
    }

    public Movie(int yearMovie, String title, String studio, String producer, String winner) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
        this.winner = winner;
    }

    public Movie(int yearMovie, String title, String studio, String producer) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
    }
}
