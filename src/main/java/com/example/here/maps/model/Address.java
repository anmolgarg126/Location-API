package com.example.here.maps.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address{

    @JsonProperty("city")
    private String city;

    @JsonProperty("postalCode")
    private String postalCode;

    @JsonProperty("county")
    private String county;

    @JsonProperty("houseNumber")
    private String houseNumber;

    @JsonProperty("label")
    private String label;

    @JsonProperty("building")
    private String building;

    @JsonProperty("countyCode")
    private String countyCode;

    @JsonProperty("countryCode")
    private String countryCode;

    @JsonProperty ("subdistrict")
    private String subdistrict;

    @JsonProperty("street")
    private String street;

    @JsonProperty("district")
    private String district;

    @JsonProperty("stateCode")
    private String stateCode;

    @JsonProperty("block")
    private String block;

    @JsonProperty("countryName")
    private String countryName;

    @JsonProperty("state")
    private String state;

    @JsonProperty("subblock")
    private String subblock;
}
