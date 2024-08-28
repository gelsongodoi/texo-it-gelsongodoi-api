package br.com.texoit.movie.controller;

import br.com.texoit.movie.dto.MovieResponseDto;
import br.com.texoit.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/findAll")
    public ResponseEntity<List<MovieResponseDto>> findAll() {
        List<MovieResponseDto> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/producers-interval")
    public Map<String, List<Map<String, Object>>> getProducersInterval() {
        return movieService.getProducersInterval();
    }

    @GetMapping("/findByYear")
    public ResponseEntity<List<MovieResponseDto>> findByYear(@RequestParam int year) {
        List<MovieResponseDto> movies = movieService.findByYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/findByWinner")
    public ResponseEntity<List<MovieResponseDto>> findByWinner() {
        List<MovieResponseDto> movies = movieService.findByWinner("yes");
        return ResponseEntity.ok(movies);
    }

}
