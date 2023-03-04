package com.example.wbtestapi.config;

import com.example.wbtestapi.client.WildberriesClient;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WildberriesClientConfig {

    @Bean
    public WildberriesClient wildberriesClient() {
        return new WildberriesClient("https://suppliers-api.wildberries.ru",
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NJRCI6IjMwNzdkODM3LTM4ZjQtNGY1OS05M2JjLWY5ZGU2YTdkNTliZiJ9.ooWx_IKwZph5q3YilwVPu4IUm-qPIkjTMQs_tN619sc");
    }
}
