package fr.hyriode.hydrion.test;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 29/03/2022 at 21:16
 */
public class Test {

    public static void main(String[] args) {

    }

    private static void get() {
        request("GET", null);
    }

    private static void post() {
        final JsonObject object = new JsonObject();

        object.addProperty("uuid", UUID.fromString("c7e68fae-32af-47ce-afbf-ee57e051a91f").toString());
        object.addProperty("name", "AstFaster");

        final String json = object.toString();
        final byte[] output = json.getBytes(StandardCharsets.UTF_8);

        request("POST", connection -> {
            try {
                connection.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
                connection.setFixedLengthStreamingMode(output.length);

                final OutputStream writer = connection.getOutputStream();

                writer.write(output);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void request(String method, Consumer<HttpURLConnection> action) {
        try {
            final long before = System.currentTimeMillis();
            final String url = "http://localhost:8080/player?uuid=c7e68fae-32af-47ce-afbf-ee57e051a91f";


            final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.addRequestProperty("API-Key", "");

            if (action != null) {
                action.accept(connection);
            }

            final StringBuilder sb = new StringBuilder();
            final int code = connection.getResponseCode();
            final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }

            br.close();

            System.out.println("Response received (" + code + "): " + sb);
            System.out.println("Request took: " + (System.currentTimeMillis() - before) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
