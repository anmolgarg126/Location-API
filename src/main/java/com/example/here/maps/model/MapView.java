package com.example.here.maps.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MapView{

    @JsonProperty ("east")
    private double east;

    @JsonProperty("south")
    private double south;

    @JsonProperty("north")
    private double north;

    @JsonProperty("west")
    private double west;
}
