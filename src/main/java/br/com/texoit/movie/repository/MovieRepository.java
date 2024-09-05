package br.com.texoit.movie.repository;

import br.com.texoit.movie.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>{

    List<Movie> findByYearMovieAndWinner(String year, String winner);

    List<Movie> findByWinner(String winner);
}
