package org.stepanov.telegrambotservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.stepanov.telegrambotservice.dto.TokenRequest;


@FeignClient(name = "auth-service", url = "${auth.service.url}")
public interface AuthServiceClient {

    @PostMapping("/tokens")
    boolean saveToken(@RequestBody TokenRequest request);
}
