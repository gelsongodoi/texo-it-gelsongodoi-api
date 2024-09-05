package br.com.texoit.movie;

import br.com.texoit.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TexoItMovieApplication implements CommandLineRunner {

    @Autowired
    private MovieService movieService;

    public static void main(String[] args) {
        SpringApplication.run(TexoItMovieApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String filePath = "src/main/resources/files/movielist.csv";
        movieService.loadMoviesFromCSV(filePath);
    }
}
