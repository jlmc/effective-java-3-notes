package io.costax.overviewmovingtojdk12.httpservers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLConnection;

public class HttpClientWithOldApi {

    public static void main(String[] args) throws IOException {

        final URI uri = URI.create("http://localhost:8080/hello");

        final URLConnection urlConnection = uri.toURL().openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

        httpURLConnection.setRequestMethod("GET");

        final int responseCode = httpURLConnection.getResponseCode();

        final String responseBody = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream())).readLine();

        System.out.println(responseCode + " : " + responseBody);
    }
}
