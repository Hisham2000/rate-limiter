package com.ratereate.shared;

import com.ratereate.service.TokenBucketService;
import feign.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class RateLimiterOncePerRequestFilter extends OncePerRequestFilter {
    private final TokenBucketService tokenBucketService;
    private final RealApiClient realApiClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        if (!tokenBucketService.tryConsume()) {
            // Rate limit exceeded, return 429 status
            // 429 refer to too many requests
            response.setStatus(429);
            response.setContentType("application/json");
            String jsonResponse = "{ \"code\": 429, \"message\": \"Rate limit exceeded\" }";
            response.getWriter().write(jsonResponse);
            response.getWriter().flush();
            return;
        }
        Response apiResponse = forwardRequestToApi(request);
        writeResponseToUser(apiResponse, response);
    }


    private Response forwardRequestToApi(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String fullPath = request.getRequestURI().substring(request.getContextPath().length());
        Map<String, String> headers = extractHeaders(request);
        Map<String, String> queryParams = extractQueryParams(request);

        if ("GET".equalsIgnoreCase(method)) {
            return realApiClient.forwardGetRequest(fullPath, queryParams, headers);
        } else if ("POST".equalsIgnoreCase(method)) {
            String body = IOUtils.toString(request.getReader());
            headers.put("Content-Type", request.getContentType());
            return realApiClient.forwardPostRequest(fullPath, body, headers);
        } else {
            throw new UnsupportedOperationException("Unsupported HTTP method: " + method);
        }
    }

    private void writeResponseToUser(Response apiResponse, HttpServletResponse response) throws IOException {
        response.setStatus(apiResponse.status());

        // Set headers from the API response
        for (Map.Entry<String, Collection<String>> entry : apiResponse.headers().entrySet()) {
            for (String value : entry.getValue()) {
                response.addHeader(entry.getKey(), value);
            }
        }

        // If the Content-Length is present, set it, otherwise let the server handle it
        if (apiResponse.headers().containsKey("Content-Length")) {
            String contentLength = apiResponse.headers().get("Content-Length").iterator().next();
            response.setContentLength(Integer.parseInt(contentLength));
        }

        // Write the body, if present
        if (apiResponse.body() != null) {
            try (InputStream inputStream = apiResponse.body().asInputStream()) {
                // Copy the API response body to the HttpServletResponse output stream
                IOUtils.copy(inputStream, response.getOutputStream());
            }
        }

        // Ensure the response is flushed to the client
        response.flushBuffer();
    }

    private Map<String, String> extractHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private Map<String, String> extractQueryParams(HttpServletRequest request) {
        Map<String, String> queryParams = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> queryParams.put(key, String.join(",", values)));
        return queryParams;
    }

}
