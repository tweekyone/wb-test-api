package com.example.wbtestapi.controller;

import com.example.wbtestapi.restclient.*;
import com.example.wbtestapi.restclient.dto.response.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ErrorsInfoController {
    private WildberriesRestClient restClient;

    @GetMapping("/errorsInfo")
    public ResponseEntity<String> getErrorsInfo() throws JsonProcessingException {
        ResponseErrorsInfo responseErrorsInfo = restClient.getErrorsInfo();
        ObjectMapper mapper = restClient.getMapper();
        String result = mapper.writeValueAsString(responseErrorsInfo);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
