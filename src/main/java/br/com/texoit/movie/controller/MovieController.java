package br.com.texoit.movie.controller;

import br.com.texoit.movie.dto.MovieResponseDto;
import br.com.texoit.movie.exception.ResourceNotFoundException;
import br.com.texoit.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "Mostra todos os produtores e seus filmes")
    public ResponseEntity<List<MovieResponseDto>> findAll() {
        List<MovieResponseDto> movies = movieService.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/producers-interval")
    @Operation(summary = "Mostra os vencedores de acordo com a especificação do projeto: " +
            "Produtor com o menor intervalo entre os prêmios e " +
            "Produtor com o maior intervalo entre os prêmios")
    public Map<String, List<Map<String, Object>>> getProducersInterval() {
        return movieService.getProducersInterval();
    }

    @GetMapping("/findByYear")
    @Operation(summary = "Mostra os vencedores, passando como parâmetro o ano do filme")
    public ResponseEntity<List<MovieResponseDto>> findByYear(@RequestParam String year) {
        List<MovieResponseDto> movies = movieService.findByYear(year);
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/findByWinner")
    @Operation(summary = "Mostra somente os vencedores")
    public ResponseEntity<List<MovieResponseDto>> findByWinner() {
        List<MovieResponseDto> movies = movieService.findByWinner("yes");
        return ResponseEntity.ok(movies);
    }

}
