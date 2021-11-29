package com.example.here.maps.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Position {

    @JsonProperty ("lng")
    private double lng;

    @JsonProperty("lat")
    private double lat;
}