package io.costax.overviewmovingtojdk12.httpservers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class HttpClientWithNewApi {

    static void main(String[] args) throws InterruptedException {

        //sendSyncRequest();

        sendAsyncRequest();

        Thread.sleep(2000);
    }

    static void sendAsyncRequest() {
        final URI uri = URI.create("http://localhost:8080/hello");

        HttpClient client = HttpClient.newHttpClient();
        System.out.println("-- " + client.version());

        final HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();


        final CompletableFuture<HttpResponse<String>> httpResponseCompletableFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        httpResponseCompletableFuture.thenAccept((HttpResponse response) -> {
            System.out.println(">" + response.statusCode());
            System.out.println(">" + response.headers());
            System.out.println(">" + response.body());
        });

        System.out.println("AsynReponse submited");
    }


    static void sendSyncRequest() throws IOException, InterruptedException {
        final URI uri = URI.create("http://localhost:8080/hello");

        HttpClient client = HttpClient.newHttpClient();
        System.out.println("-- " + client.version());

        final HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();


        final HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


        final int responseStatus = response.statusCode();
        final HttpHeaders headers = response.headers();
        final String body = response.body();

        System.out.println(">" + responseStatus);
        System.out.println(">" + headers);
        System.out.println(">" + body);
    }
}
