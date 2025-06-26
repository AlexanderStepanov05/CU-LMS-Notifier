package org.notifier.botservice.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "parser-service", url = "${services.parser.url}")
public interface ParserServiceClient {
    @GetMapping("/api/tasks")
    List<Object> getTasks(@RequestHeader("Cookie") String cookieHeader);
}
