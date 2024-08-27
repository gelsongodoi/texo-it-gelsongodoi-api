package br.com.texoit.movie.controller;

import br.com.texoit.movie.dto.AwardIntervalDto;
import br.com.texoit.movie.model.Movie;
import br.com.texoit.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Movie>> findAll() {
        List<Movie> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/producers-interval")
    public ResponseEntity<List<AwardIntervalDto>> getProducersWithBiggestAndShortestRange() {
        List<AwardIntervalDto> producersWithBiggestAndShortestRange = movieService.getProducersInterval();
        return ResponseEntity.ok(producersWithBiggestAndShortestRange);
    }

    @GetMapping("/findByYear")
    public ResponseEntity<List<Movie>> findByYear(@RequestParam int year) {
        List<Movie> movies = movieService.findByYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/findByWinner")
    public ResponseEntity<List<Movie>> findByWinner() {
        List<Movie> movies = movieService.findByWinner("yes");
        return ResponseEntity.ok(movies);
    }

}
