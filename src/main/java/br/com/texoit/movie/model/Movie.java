package br.com.texoit.movie.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_movie")
public class Movie {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "year")
    private String yearMovie;

    @Column(name = "title")
    private String title;

    @Column(name = "studio")
    private String studio;

    @Column(name = "producer")
    private String producer;

    @Column(name = "winner")
    private String winner;

    public Movie() {
    }

    public Movie(Long id, String yearMovie, String title, String studio, String producer, String winner) {
        this.id = id;
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
        this.winner = winner;
    }

    public Movie(String yearMovie, String title, String studio, String producer, String winner) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
        this.winner = winner;
    }

    public Movie(String yearMovie, String title, String studio, String producer) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studio = studio;
        this.producer = producer;
    }
}
