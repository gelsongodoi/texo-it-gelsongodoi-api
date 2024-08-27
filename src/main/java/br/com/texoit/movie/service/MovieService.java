package br.com.texoit.movie.service;

import br.com.texoit.movie.dto.MovieRequestDto;
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

    public Map<String, List<Map<String, Object>>> getProducersInterval() {
        if (movieRepository.count() == 0){
            loadCSV();
        }
        List<Movie> movies = movieRepository.findAll();
        Map<String, List<Integer>> producerYearsMap = new HashMap<>();

        // Organizar anos por produtor
        for (Movie movie : movies) {
            producerYearsMap
                    .computeIfAbsent(movie.getProducer(), k -> new ArrayList<>())
                    .add(movie.getYearMovie());
        }

        List<Map<String, Object>> minProducers = new ArrayList<>();
        List<Map<String, Object>> maxProducers = new ArrayList<>();

        int minInterval = Integer.MAX_VALUE;
        int maxInterval = 0;

        // Calcular os intervalos
        for (Map.Entry<String, List<Integer>> entry : producerYearsMap.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            Collections.sort(years); // Garantir que os anos estejam em ordem

            if (years.size() < 2) continue; // Precisamos de pelo menos 2 prêmios para calcular o intervalo

            int previousYear = years.get(0);
            int producerMinInterval = Integer.MAX_VALUE;
            int producerMaxInterval = Integer.MIN_VALUE;
            int firstWinYear = years.get(0);
            int secondWinYear = years.get(1);
            int producerFastestInterval = secondWinYear - firstWinYear;

            for (int i = 1; i < years.size(); i++) {
                int currentYear = years.get(i);
                int interval = currentYear - previousYear;

                // Atualizar intervalo máximo
                if (interval > producerMaxInterval) {
                    producerMaxInterval = interval;
                }

                // Atualizar intervalo mínimo
                if (interval < producerMinInterval) {
                    producerMinInterval = interval;
                }

                previousYear = currentYear;
            }

            // Atualizar a lista de produtores com a lógica mínima
            if (producerMinInterval < minInterval) {
                minInterval = producerMinInterval;
                minProducers.clear();
                minProducers.add(createProducerMap(producer, producerMinInterval, firstWinYear, firstWinYear + producerMinInterval));
            } else if (producerMinInterval == minInterval) {
                minProducers.add(createProducerMap(producer, producerMinInterval, firstWinYear, firstWinYear + producerMinInterval));
            }

            // Atualizar a lista de produtores com a lógica máxima
            if (producerMaxInterval > maxInterval) {
                maxInterval = producerMaxInterval;
                maxProducers.clear();
                maxProducers.add(createProducerMap(producer, producerMaxInterval, years.get(0), years.get(0) + producerMaxInterval));
            } else if (producerMaxInterval == maxInterval) {
                maxProducers.add(createProducerMap(producer, producerMaxInterval, years.get(0), years.get(0) + producerMaxInterval));
            }
        }

        // Montar o resultado final
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minProducers);
        result.put("max", maxProducers);
        return result;
    }

    private Map<String, Object> createProducerMap(String producer, int interval, int previousWin, int followingWin) {
        Map<String, Object> producerMap = new HashMap<>();
        producerMap.put("producer", producer);
        producerMap.put("interval", interval);
        producerMap.put("previousWin", previousWin);
        producerMap.put("followingWin", followingWin);
        return producerMap;
    }
}
