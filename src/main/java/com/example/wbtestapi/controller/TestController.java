package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.WildberriesRestClient;
import com.example.wbtestapi.restclient.dto.request.RequestCardList;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final WildberriesRestClient wildberriesRestClient;

    @SneakyThrows
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        int limit = 100;
        RequestCardList requestCardList = new RequestCardList(
                new RequestCardList.Sort(new RequestCardList.Cursor(limit), new RequestCardList.Filter(-1)));
        var result = wildberriesRestClient.getCardList(requestCardList);
        String json = wildberriesRestClient.getMapper().writeValueAsString(result);
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
}
