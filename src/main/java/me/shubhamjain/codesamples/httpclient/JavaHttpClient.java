package me.shubhamjain.codesamples.httpclient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class JavaHttpClient {
    private static final Logger log = LoggerFactory.getLogger(JavaHttpClient.class);

    public static void post(String body, String baseUrl, String apiPath, String queryParams, String... headers) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + apiPath + queryParams)).POST(HttpRequest.BodyPublishers.ofString(body)).headers(headers).build();
        HttpClientFactory.getHttpClientInstance().sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenApply((a) -> {
            if (a.statusCode() == 404) {
                log.error("Error404 : request body {} with status code {} and response {}", new Object[]{body, a.statusCode(), a.body()});
                return CompletableFuture.completedFuture((Object)null);
            } else if (a.statusCode() >= 400 && a.statusCode() < 500) {
                log.error("Error4XX : request body {} with status code {} and response {}", new Object[]{body, a.statusCode(), a.body()});
                return CompletableFuture.completedFuture((Object)null);
            } else if (a.statusCode() >= 500 && a.statusCode() < 600) {
                log.error("Error5XX : request body {} with status code {} and response {}", new Object[]{body, a.statusCode(), a.body()});
                return CompletableFuture.completedFuture((Object)null);
            } else {
                log.debug("Posted successfully with response body {} and status {}", a.body(), a.statusCode());
                return CompletableFuture.completedFuture((Object)null);
            }
        });
    }
}
