package com.example.here.maps.controller;

import com.example.here.maps.client.HereMapsClient;
import com.example.here.maps.model.Address;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocationServiceController {
    private final HereMapsClient hereMapsClient;


    @Operation (summary = "Get address by location(Latitude, Longitude)")
    @ApiResponses (value = {
            @ApiResponse (responseCode = "200", description = "Found address",
                    content = {@Content (mediaType = "application/json",
                            schema = @Schema (implementation = Address.class))}),
            @ApiResponse (responseCode = "500", description = "Some Error while fetching details",
                    content = @Content)})
    @GetMapping ("/get-address-from-location")
    public ResponseEntity<Address> getLocation(@RequestParam ("latitude") @NotNull
//                                                   @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
                                                       String latitude,
                                               @RequestParam ("longitude") @NotNull
//                                               @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
                                                       String longitude) {
        log.info("Getting address for the latitude :: {} and longitude :: {}", latitude, longitude);
        val address = hereMapsClient.getAddressFromLocation(latitude, longitude);
        return new ResponseEntity<>(address.orElse(null), address.isPresent() ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


