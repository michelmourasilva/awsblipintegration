package br.com.vwco.chatbot.blip.datamart.integration;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.LocalDate;

/**
 * Methods return the request body
 */
public class BlipClient {

    private final HttpClient httpClient;
    private final Config config;

    public BlipClient(
            HttpClient httpClient,
            Config config
    ) {
        this.httpClient = httpClient;
        this.config = config;
    }


    public String fetchCategories(LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        final var requestBody = "{" +
                " \"id\": \"requestid\", " +
                " \"to\": \"postmaster@analytics.msging.net\", " +
                " \"method\": \"get\", " +
                " \"uri\": \"/event-track?startDate=" + startDate + "&endDate=" + endDate + "&$take=10\"" +
                "}";

        System.out.println("-------> Req" + requestBody);
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(config.blipUri))
                .header("Authorization", config.blipKey)
                .header("Content-Type", "application/json")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final var body = response.body();

        System.out.println("------- Rest> " + requestBody);
        return body;
    }

    public String fetchActionsByCategory(String category, LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        final var requestBody = "{" +
                " \"id\": \"requestid\", " +
                " \"to\": \"postmaster@analytics.msging.net\", " +
                " \"method\": \"get\", " +
                " \"uri\": \"/event-track/" + URLEncoder.encode(category, Charset.defaultCharset()) +
                "?startDate=" + startDate + "&endDate=" + endDate + "&$take=10\"" +
                "}";

        System.out.println("-------> " + requestBody);
        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(config.blipUri))
                .header("Authorization", config.blipKey)
                .header("Content-Type", "application/json")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final var body = response.body();

        System.out.println("--------- Res> " + body);
        return body;
    }

    public String fetchEvents(String category, String action, LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {

        final var requestBody = "{" +
                " \"id\": \"requestid\", " +
                " \"to\": \"postmaster@analytics.msging.net\", " +
                " \"method\": \"get\", " +
                " \"uri\": \"/event-track/" +
                URLEncoder.encode(category, Charset.defaultCharset()) +
                "/" +
                URLEncoder.encode(action, Charset.defaultCharset()) +
                "?startDate=" +
                startDate +
                "&endDate=" +
                endDate +
                "&$take=10\"" +
                "}";

        System.out.println("+++++++++++ Req> " + requestBody);

        var request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .uri(URI.create(config.blipUri))
                .header("Authorization", config.blipKey)
                .header("Content-Type", "application/json")
                .build();

        final var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        final var body = response.body();


        System.out.println("+++++++++++ Res> " + body);

        return body;

    }
}
