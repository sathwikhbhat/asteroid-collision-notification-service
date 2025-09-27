package com.sathwikhbhat.astro_alert.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NasaNeoResponse {

    @JsonProperty("near_earth_objects")
    private Map<String, List<Asteroids>> nearEarthObjects;

    @JsonProperty("element_count")
    private Long totalAsteroids;

}