package br.com.texoit.movie.service;

import br.com.texoit.movie.dto.*;
import br.com.texoit.movie.exception.ResourceNotFoundException;
import br.com.texoit.movie.model.Movie;
import br.com.texoit.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieResponseDto> findAll() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponseDto> movieResponseDtos = MovieResponseDto.converterList(movies);
        return movieResponseDtos;
    }

    public List<MovieResponseDto> findByYear(String year) {
        List<Movie> movies = movieRepository.findByYearMovieAndWinner(year, "yes");
        if (movies.isEmpty())
            throw new ResourceNotFoundException("Não há dados a exibir com esta informação: ano " + year);

        List<MovieResponseDto> movieResponseDtos = MovieResponseDto.converterList(movies);
        return movieResponseDtos;
    }

    public List<MovieResponseDto> findByWinner(String winner) {
        List<Movie> movies = movieRepository.findByWinner(winner);
        List<MovieResponseDto> movieResponseDtos = MovieResponseDto.converterList(movies);
        return movieResponseDtos;
    }

    public Map<String, List<Map<String, Object>>> getProducersInterval() {
        List<Movie> movies = movieRepository.findByWinner("yes");
        Map<String, List<Integer>> producerYearsMap = new HashMap<>();

        // Organizar anos por produtor (separando por ',' e 'and')
        for (Movie movie : movies) {
            String[] producers = movie.getProducer().split(" and |, and |, ");
            int year = Integer.parseInt(movie.getYearMovie());
            for (String producer : producers) {
                producer = producer.trim();
                producerYearsMap
                        .computeIfAbsent(producer, k -> new ArrayList<>())
                        .add(year);
            }
        }

        List<Map<String, Object>> minProducers = new ArrayList<>();
        List<Map<String, Object>> maxProducers = new ArrayList<>();
        Set<String> addedProducers = new HashSet<>(); // Para evitar duplicatas

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
                //minProducers.clear(); // Limpa a lista antes de adicionar novos
                minProducers.add(createProducerMap(producer, producerMinInterval, firstWinYear, firstWinYear + producerMinInterval));
                addedProducers.add(producer); // Marcar como adicionado
            } else if (producerMinInterval == minInterval && minProducers.size() < 2) {
                minProducers.add(createProducerMap(producer, producerMinInterval, firstWinYear, firstWinYear + producerMinInterval));
                addedProducers.add(producer); // Marcar como adicionado
            }

            // Atualizar a lista de produtores com a lógica máxima
            if (producerMaxInterval > maxInterval) {
                maxInterval = producerMaxInterval;
                //maxProducers.clear(); // Limpa a lista antes de adicionar novos
                // Antes de adicionar, verifique se o produtor já foi adicionado nos mínimos
                if (!addedProducers.contains(producer)) {
                    maxProducers.add(createProducerMap(producer, producerMaxInterval, years.get(0), years.get(0) + producerMaxInterval));
                    addedProducers.add(producer); // Marcar como adicionado
                }
            } else if (producerMaxInterval == maxInterval && maxProducers.size() < 2) {
                // Adiciona apenas se não foi adicionado nos mínimos
                if (!addedProducers.contains(producer)) {
                    maxProducers.add(createProducerMap(producer, producerMaxInterval, years.get(0), years.get(0) + producerMaxInterval));
                    addedProducers.add(producer); // Marcar como adicionado
                }
            }
        }

        // Ordenar os resultados por intervalo
        minProducers.sort(Comparator.comparingInt(entry -> (int) entry.get("interval")));
        maxProducers.sort(Comparator.comparingInt(entry -> (int) entry.get("interval")));

        // Montar o resultado final
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        result.put("min", minProducers);
        result.put("max", maxProducers);
        return result;
    }

    private Map<String, Object> createProducerMap(String producer, int interval, int previousWin, int followingWin) {
        Map<String, Object> producerMap = new LinkedHashMap<>(); // LinkedHashMap para manter a ordem de inserção
        producerMap.put("producer", producer);
        producerMap.put("interval", interval);
        producerMap.put("previousWin", previousWin);
        producerMap.put("followingWin", followingWin);
        return producerMap;
    }

    public void loadMoviesFromCSV(String filePath) {

        String line;
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(";");
                if(i == 0) {
                    i++;
                    continue;
                }
                    Movie movie = new Movie();
                    if (fields.length == 4) {
                        movie.setYearMovie(fields[0]); // Ano
                        movie.setTitle(fields[1]); // Título
                        movie.setStudio(fields[2]); // Estúdio
                        movie.setProducer(fields[3]); // Produtor
                    }
                    if (fields.length == 5){
                        movie.setYearMovie(fields[0]); // Ano
                        movie.setTitle(fields[1]); // Título
                        movie.setStudio(fields[2]); // Estúdio
                        movie.setProducer(fields[3]); // Produtor
                        movie.setWinner(fields[4]); // Vencedor
                    }
                    movieRepository.save(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
