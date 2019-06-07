package io.costax.overviewmovingtojdk12.httpservers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class SimpleHTTPServerUsingJDK {

    public static void main(String[] args) throws IOException {

        HttpHandler httpHandler = new HttpHandler() {
            @Override
            public void handle(final HttpExchange exchange) throws IOException {

                String body = "simple http server : " + System.currentTimeMillis();

                exchange.sendResponseHeaders(200, body.length());

                try (final OutputStream responseBody = exchange.getResponseBody()) {

                    responseBody.write(body.getBytes(StandardCharsets.UTF_8));

                }
            }
        };

        final InetSocketAddress address = new InetSocketAddress(8080);
        final HttpServer httpServer = HttpServer.create(address, 0);
        httpServer.createContext("/hello", httpHandler);

        httpServer.start();
    }
}
