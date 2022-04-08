package fr.hyriode.hydrion.client.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Project: Hydrion
 * Created by AstFaster
 * on 04/04/2022 at 20:41
 */
public class HttpParameters {

    private final Map<String, Object> objects;

    public HttpParameters() {
        this.objects = new HashMap<>();
    }

    public HttpParameters add(String key, Object object) {
        this.objects.put(key, object);
        return this;
    }

    public String asQueryString() {
        final StringBuilder queryString = new StringBuilder();

        boolean queryStarted = false;

        for (Map.Entry<String, Object> entry : this.objects.entrySet()) {
            if (!queryStarted) {
                queryStarted = true;

                queryString.append("?");
            } else {
                queryString.append("&");
            }

            queryString.append(entry.getKey())
                    .append("=").
                    append(entry.getValue());
        }
        return queryString.toString();
    }

    public static HttpParameters create() {
        return new HttpParameters();
    }

}
