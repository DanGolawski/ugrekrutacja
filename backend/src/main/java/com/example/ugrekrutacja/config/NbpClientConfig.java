package com.example.ugrekrutacja.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class NbpClientConfig {

    @Bean
    public RestClient nbpRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl("https://api.nbp.pl/api")
                .build();
    }
}
