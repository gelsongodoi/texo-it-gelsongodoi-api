package br.com.texoit.movie.service;

import br.com.texoit.movie.dto.MovieRequestDto;
import br.com.texoit.movie.dto.MovieResponseDto;
import br.com.texoit.movie.exception.ResourceNotFoundException;
import br.com.texoit.movie.model.Movie;
import br.com.texoit.movie.repository.MovieRepository;
import br.com.texoit.movie.util.MovieCSVLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @InjectMocks
    private MovieService movieService;

    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadCSV() {
        // Testa o método loadCSV para garantir que os dados podem ser carregados
        when(movieRepository.count()).thenReturn(0L);

        // Simule o carregamento CSV
        List<MovieRequestDto> movieRequestDtos = Arrays.asList(
                new MovieRequestDto(1980, "Can't Stop the Music", "Associated Film Distribution", "Allan Carr", "yes"),
                new MovieRequestDto(1984, "Bolero", "Cannon Films", "Bo Derek", "yes"),
                new MovieRequestDto(1990, "Ghosts Can't Do It", "Triumph Releasing", "Bo Derek, Andrew Bergman", "yes"),
                new MovieRequestDto(1996, "Striptease", "Columbia Pictures", "Buzz Feitshans", "yes")
        );

        // Aqui você deve simular o retorno do MovieCSVLoader.loadCSV
        try (MockedStatic<MovieCSVLoader> mocked = mockStatic(MovieCSVLoader.class)) {
            mocked.when(MovieCSVLoader::loadCSV).thenReturn(movieRequestDtos);
            movieService.loadCSV();
            verify(movieRepository, times(2)).save(any(Movie.class));
        }
    }

    @Test
    void testFindAllWhenEmpty() {
        // Testa se loadCSV é chamado quando não há filmes
        when(movieRepository.count()).thenReturn(0L);
        when(movieRepository.findAll()).thenReturn(Collections.emptyList());

        List<MovieResponseDto> movies = movieService.findAll();
        assertTrue(movies.isEmpty());
        verify(movieRepository, times(1)).count();
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void testFindAllWhenNotEmpty() {
        // Testa a recuperação dos filmes quando já existem no repositório
        Movie movie = new Movie(2021, "Movie A", "Studio A", "Producer A", "yes");
        when(movieRepository.count()).thenReturn(1L);
        when(movieRepository.findAll()).thenReturn(Collections.singletonList(movie));

        List<MovieResponseDto> movies = movieService.findAll();
        assertEquals(1, movies.size());
        assertEquals("Movie A", movies.get(0).getTitle());
    }

    @Test
    void testFindByYearWithExistingMovies() {
        // Testa a busca por ano quando há filmes disponíveis
        Movie movie = new Movie(2021, "Movie A", "Studio A", "Producer A", "yes");
        when(movieRepository.count()).thenReturn(1L);
        when(movieRepository.findByYearMovieAndWinner(2021, "yes")).thenReturn(Collections.singletonList(movie));

        List<MovieResponseDto> result = movieService.findByYear(2021);
        assertEquals(1, result.size());
        assertEquals("Movie A", result.get(0).getTitle());
    }

    @Test
    void testFindByYearWithNoMovies() {
        // Testa a exceção quando não há filmes encontrados para um ano específico
        when(movieRepository.count()).thenReturn(1L);
        when(movieRepository.findByYearMovieAndWinner(2024, "yes")).thenReturn(Collections.emptyList());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            movieService.findByYear(2024);
        });

        assertEquals("Não há dados a exibir com esta informação: ano 2024", thrown.getMessage());
    }

    @Test
    void testGetProducersInterval() {
        // Testa o cálculo de intervalos para produtores
        List<Movie> movies = Arrays.asList(
                new Movie(1,1980, "Can't Stop the Music", "Associated Film Distribution", "Allan Carr", "yes"),
                new Movie(2,1984, "Bolero", "Cannon Films", "Bo Derek", "yes"),
                new Movie(3,1990, "Ghosts Can't Do It", "Triumph Releasing", "Bo Derek, Andrew Bergman", "yes"),
                new Movie(4,1996, "Striptease", "Columbia Pictures", "Buzz Feitshans", "yes")
        );

        when(movieRepository.count()).thenReturn(1L);
        when(movieRepository.findByWinner("yes")).thenReturn(movies);

        Map<String, List<Map<String, Object>>> result = movieService.getProducersInterval();

        assertNotNull(result);
        assertTrue(result.containsKey("min"));
        assertTrue(result.containsKey("max"));
    }

}
