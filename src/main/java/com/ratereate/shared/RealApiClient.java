package com.ratereate.shared;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "realApiClient", url = "${real.api.url}")
public interface RealApiClient {
    @GetMapping(value = "{path}")
    Response forwardGetRequest(@PathVariable("path") String path,
                               @RequestParam Map<String, String> queryParams,
                               @RequestHeader Map<String, String> headers);

    @PostMapping(value = "{path}")
    Response forwardPostRequest(@PathVariable("path") String path,
                                @RequestBody String body,
                                @RequestHeader Map<String, String> headers);
}