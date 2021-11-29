package com.example.here.maps.client;

import com.example.here.maps.model.Address;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component ("hereMapsClient")
public class HereMapsClient {
    @Value ("${here.maps.api.key}")
    private String apiKey;

    @Value ("${here.maps.api.url.for.address.from.location}")
    private String apiUrlForAddressFromLocation;

    @Value ("${here.maps.api.connect.timeout}")
    private int connectTimeoutInMillis;
    @Value ("${here.maps.api.connect.request.timeout}")
    private int connectionRequestTimeoutInMillis;
    @Value ("${here.maps.api.socket.timeout}")
    private int socketTimeoutInMillis;

    private RequestConfig requestConfig;
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Init.
     */
    @EventListener (ApplicationStartedEvent.class)
    public void init() {
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(connectTimeoutInMillis)
                .setConnectionRequestTimeout(connectionRequestTimeoutInMillis)
                .setSocketTimeout(socketTimeoutInMillis)
                .build();
    }

    /**
     * Get address from location (latitude and longitude)
     *
     * @param latitude   the latitude
     * @param longitude  the longitude
     * @return the optional address
     */
    @SneakyThrows
    public Optional<Address> getAddressFromLocation(String latitude, String longitude) {
        val query = latitude + "," + longitude;
        log.info("GetAddressFromLocation initiated for location :: {}", query);
        try (val client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()) {
            val httpGet = new HttpGet(apiUrlForAddressFromLocation + "?apiKey=" + apiKey + "&at=" + query + "&lang=en-US&limit=1");
            val uri = new URIBuilder(httpGet.getURI()).build();
            httpGet.setURI(uri);

            val response = client.execute(httpGet);
            log.info("Response from Service :: {}", response);
            return Optional.ofNullable(response)
                    .filter(r -> r.getStatusLine().getStatusCode() == 200)
                    .map(HttpResponse::getEntity)
                    .map(this::createAddressResponseFromHttpResponse);
        } catch (Exception e) {
            log.error("Error while getting address from location :: {} with message :: {}", e, e.getMessage());
            return Optional.empty();
        }

    }

    /**
     * Create address response from http response address.
     *
     * @param response the response
     * @return the address
     */
    @SneakyThrows
    private Address createAddressResponseFromHttpResponse(HttpEntity response) {
        val addressNode = mapper.readTree(response.getContent())
                .get("items")
                .get(0)
                .get("address");

        log.info("Address node from here maps :: {}", addressNode);

        String label = addressNode.get("label").asText();
        log.info("Address label from here maps :: {}", label);

        val address = new Address()
                .setLabel(label)
                .setCity(getValueFromNode(addressNode,"city"))
                .setCountryCode(getValueFromNode(addressNode,"countryCode"))
                .setCounty(getValueFromNode(addressNode,"county"))
                .setCountryName(getValueFromNode(addressNode,"countryName"))
                .setDistrict(getValueFromNode(addressNode,"district"))
                .setSubdistrict(getValueFromNode(addressNode,"subdistrict"))
                .setHouseNumber(getValueFromNode(addressNode,"houseNumber"))
                .setBuilding(getValueFromNode(addressNode,"building"))
                .setStreet(getValueFromNode(addressNode,"street"))
                .setBlock(getValueFromNode(addressNode,"block"))
                .setSubblock(getValueFromNode(addressNode,"subblock"))
                .setPostalCode(getValueFromNode(addressNode,"postalCode"))
                .setStateCode(getValueFromNode(addressNode,"stateCode"))
                .setState(getValueFromNode(addressNode,"state"));

        log.info("App Response from here maps response for getting address from location :: {}", address);
        return address;
    }

    private String getValueFromNode(JsonNode node, String key) {
        return Optional.ofNullable(node.get(key))
                .map(JsonNode::asText)
                .orElse(null);
    }

}