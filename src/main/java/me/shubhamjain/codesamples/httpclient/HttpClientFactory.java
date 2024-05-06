package me.shubhamjain.codesamples.httpclient;

import java.net.http.HttpClient;

public class HttpClientFactory {
    public static HttpClient getHttpClientInstance() {
        return HttpClientFactory.HttpClientFactoryHelper.INSTANCE;
    }

    private HttpClientFactory() {
    }

    private static class HttpClientFactoryHelper {
        private static final HttpClient INSTANCE = HttpClient.newHttpClient();

        private HttpClientFactoryHelper() {
        }
    }
}
