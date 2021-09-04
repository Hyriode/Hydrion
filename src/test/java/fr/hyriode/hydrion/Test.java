package fr.hyriode.hydrion;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/09/2021 at 11:31
 */
public class Test {

    public static void main(String[] args) throws IOException {
        final JsonObject object = new JsonObject();

        object.addProperty("test", "Hello");
        object.addProperty("second", "World");

        final String url = "http://localhost:8080";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        final OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
        wr.write(object.toString());
        wr.flush();

        StringBuilder sb = new StringBuilder();
        int HttpResult = connection.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            final BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            System.out.println("" + sb);
        } else {
            System.out.println(connection.getResponseMessage());
        }
    }

}
