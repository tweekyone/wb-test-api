package com.example.wbtestapi.restclient;

import com.example.wbtestapi.client.exception.*;
import com.example.wbtestapi.restclient.dto.request.*;
import com.example.wbtestapi.restclient.dto.response.ProductCodeResponse;
import com.example.wbtestapi.restclient.dto.response.*;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.joda.*;
import com.fasterxml.jackson.datatype.jsr310.*;
import lombok.*;
import org.springframework.boot.web.client.*;
import org.springframework.http.*;
import org.springframework.http.client.*;
import org.springframework.stereotype.*;
import org.springframework.web.client.*;
import org.springframework.web.util.*;

import java.text.*;
import java.util.*;

@Getter
@Component
public class WildberriesRestClient {

    private final String url = "https://suppliers-api.wildberries.ru/";
    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6IjMwNzdkODM3LTM4ZjQtNGY1OS05M2JjLWY5ZGU2YTdkNTliZiJ9.ooWx_IKwZph5q3YilwVPu4IUm-qPIkjTMQs_tN619sc";
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    public WildberriesRestClient() {
        this.restTemplate = createRestClient();
        this.mapper = createMapper();
    }

    private RestTemplate createRestClient() {
        return new RestTemplateBuilder().rootUri(url)
                .additionalInterceptors((ClientHttpRequestInterceptor) (request, body, execution) -> {
                    request.getHeaders().add("Authorization", "Bearer " + token);
                    return execution.execute(request, body);
                }).build();
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JodaModule());
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")); //2022-01-25T23:42:39Z
        mapper.enable(DeserializationFeature.USE_LONG_FOR_INTS);
        return mapper;
    }

    public ResponseCardList getCardList(RequestCardList requestCardList) {
        var result = restTemplate.postForEntity(
                "/content/v1/cards/cursor/list",
                requestCardList,
                ResponseCardList.class);
        return result.getBody();
    }

    public ProductCodeResponse getProductCodeResponse(ProductCodeRequest productCodeRequest) {
        var result = restTemplate.postForEntity(
                "/content/v1/cards/filter",
                productCodeRequest,
                ProductCodeResponse.class);
        return result.getBody();
    }

    public List<ResponsePricesInfo> getPricesInfo(int quantity) {
        var result = restTemplate.getForEntity(
                "/public/api/v1/info?quantity={0}",
                ResponsePricesInfo[].class,
                quantity);
        return Arrays.asList(result.getBody());
    }

    public ResponseErrorsInfo getErrorsInfo() {
        var result = restTemplate.getForEntity("/content/v1/cards/error/list",
                ResponseErrorsInfo.class);
        return result.getBody();
    }

    public HttpStatusCode refreshStocks(Long warehouseId, RequestStocks stocks) {
        try {
            ResponseEntity<Void> result = restTemplate.exchange(
                    "/api/v3/stocks/{0}",
                    HttpMethod.PUT,
                    getHttpEntityStocks(stocks),
                    Void.class,
                    warehouseId);
            return result.getStatusCode();
        } catch (HttpClientErrorException ex) {
            ResponseStockError responseBodyAs = ex.getResponseBodyAs(ResponseStockError.class);
            return ex.getStatusCode();
        }
    }

    private HttpEntity<RequestStocks> getHttpEntityStocks (RequestStocks stocks) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<RequestStocks>(stocks, headers);
    }

    private byte[] json(Object object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new WildberriesException(e.getMessage(), e);
        }
    }

}
