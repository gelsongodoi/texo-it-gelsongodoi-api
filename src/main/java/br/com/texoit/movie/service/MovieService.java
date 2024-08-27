package br.com.texoit.movie.service;

import br.com.texoit.movie.dto.AwardIntervalDto;
import br.com.texoit.movie.dto.MovieProducersDto;
import br.com.texoit.movie.dto.MovieRequestDto;
import br.com.texoit.movie.dto.MovieResponseDto;
import br.com.texoit.movie.model.Movie;
import br.com.texoit.movie.repository.MovieRepository;
import br.com.texoit.movie.util.MovieCSVLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public void loadCSV(){
        List<MovieRequestDto> movieRequestDtos = MovieCSVLoader.loadCSV();
        for (MovieRequestDto movieRequestDto : movieRequestDtos) {
            Movie movie = new Movie(movieRequestDto.getYearMovie(), movieRequestDto.getTitle(), movieRequestDto.getStudio(), movieRequestDto.getProducer(), movieRequestDto.getWinner());
            movieRepository.save(movie);
        }
    }

    public List<Movie> findAll() {
        if (movieRepository.count() == 0){
            loadCSV();
        }
        return movieRepository.findAll();
    }

    public List<Movie> findByYear(int year) {
        if (movieRepository.count() == 0){
            loadCSV();
        }
        List<Movie> awards = movieRepository.findByYearMovieAndWinner(year, "yes");
        return awards;
    }

    public List<Movie> findByWinner(String winner) {
        if (movieRepository.count() == 0){
            loadCSV();
        }
        List<Movie> awards = movieRepository.findByWinner(winner);
        return awards;
    }

    public List<AwardIntervalDto> getProducersInterval() {
        if (movieRepository.count() == 0){
            loadCSV();
        }
        List<Movie> awards = findByWinner("yes");

        List<MovieResponseDto> producersWithLesserInterval = new ArrayList<>();
        List<MovieResponseDto> producersWithLargestInterval = new ArrayList<>();
        List<MovieProducersDto> previousAward = new ArrayList<>();
        List<MovieProducersDto> producers = new ArrayList<>();

        for (int i = 1; i < awards.size(); i++) {
            String producer = awards.get(i).getProducer();
            String[] p = producer.split(" and |, and |, ");
            int j = 0;
            for (String s : p) {
                producers.add(j, new MovieProducersDto(s, awards.get(i).getYearMovie()));
                j++;
            }

            //WorstMovie previousAward = awards.get(i);
            previousAward.add(new MovieProducersDto(awards.get(i -1).getProducer(), awards.get(i -1).getYearMovie()));

            /*for (int k = 0; k < awards.size(); k++) {
                //WorstMovie compareAward = awards.get(k);
                compareAward.add(new WorstMovieProducersDto(producers.get(i).getNameProducer(), producers.get(i).getYearMovie()));
                if (previousAward.get(i -1).getNameProducer().equals(compareAward.get(k +1).getNameProducer())) {
                    producersWithLesserInterval.add(new WorstMovieResponseDto(
                            previousAward.get(i -1).getNameProducer(),
                            previousAward.get(i -1).getYearMovie()
                    ));
                    long interval = compareAward.get(i -1).getYearMovie() - previousAward.get(k +1).getYearMovie();
 *//*                   if (previousAward.isEmpty() || interval < previousAward.get(0).getInterval()) {
                        previousAward.clear();
                        previousAward.add(new WorstMovieResponseDto(
                                previousAward.getProducer(),
                                interval,
                                previousAward.getYearMovie(),
                                compareAward.getYearMovie()
                        ));
                    }
                    if (producersWithLargestInterval.isEmpty() || interval > producersWithLargestInterval.get(0).getInterval()) {
                        producersWithLargestInterval.clear();
                        producersWithLargestInterval.add(new WorstMovieResponseDto(
                                previousAward.getProducer(),
                                interval,
                                previousAward.getYearMovie(),
                                compareAward.getYearMovie()
                        ));
                    }*//*
                }

            }*/
        }

        for (MovieProducersDto producer : producers) {
            System.out.println(producer.getNameProducer() + " " + producer.getYearMovie());
        }

        for (MovieProducersDto producer : previousAward) {
            System.out.println("previous: " + producer.getNameProducer() + " - " + producer.getYearMovie());
        }
        return Collections.singletonList(new AwardIntervalDto(producersWithLesserInterval, producersWithLargestInterval));
    }
}
