package fr.hyriode.hydrion.test;

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
 * on 29/03/2022 at 21:16
 */
public class Test {

    public static void main(String[] args) throws IOException {

        final String url = "http://localhost:8080/player?uuid=c7e68fae-32af-47ce-afbf-ee57e051a91f";

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

        connection.setDoOutput(true);
        connection.setRequestMethod("GET");

        connection.addRequestProperty("API-Key", "c7e68fae-32af-47ce-afbf-ee57e051a91f");

        final OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
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
