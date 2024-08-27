package br.com.texoit.movie.util;

import br.com.texoit.movie.dto.MovieRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class MovieCSVLoader {

    @Value("${app.files.movielist}")
    //private static String movieListPath;

    private static String movieListPath = "src/main/resources/files/movielist.csv";

    public static List<MovieRequestDto> loadCSV() {

        log.info("Vai ler o arquivo ****************** " + movieListPath);

        List<MovieRequestDto> movieRequestDtos = new ArrayList<>();

        if (movieListPath == null){
            log.warn("error ************************************************ ");
        }

        String csvFile = movieListPath;
        String line = "";
        String cvsSplitBy = ";";
        int iteration = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                if(iteration == 0) {
                    iteration++;
                    continue;
                }
                String[] movie = line.split(cvsSplitBy);
                if (movie.length == 5) {
                    movieRequestDtos.add(new MovieRequestDto(Integer.parseInt(movie[0]), movie[1], movie[2], movie[3], movie[4]));
                } else if (movie.length == 4) {
                    String[] newMovie = new String[4];
                    for (int i = 0; i < 4; i++) {
                        newMovie[i] = movie[i];
                    }
                    movieRequestDtos.add(new MovieRequestDto(Integer.parseInt(newMovie[0]), newMovie[1], newMovie[2], newMovie[3], ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movieRequestDtos;
    }
}
