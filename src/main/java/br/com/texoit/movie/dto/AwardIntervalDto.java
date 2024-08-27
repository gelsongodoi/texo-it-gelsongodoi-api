package br.com.texoit.movie.dto;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
public class AwardIntervalDto {

    private List<MovieResponseDto> min;
    private List<MovieResponseDto> max;

    public AwardIntervalDto(List<MovieResponseDto> min, List<MovieResponseDto> max) {
        this.min = min;
        this.max = max;
    }
}
